package edu.escuelaing.arep;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.escuelaing.arep.model.User;
import spark.Request;

import java.util.ArrayList;
import java.util.HashMap;

import static spark.Spark.*;

/**
 * Login Server class
 * @author Laura García
 * @version 1.0
 */
public class App {

    private static HashMap<String, Integer> users = new HashMap<String, Integer>();

    /**
     * Main method
     * @param args
     */
    public static void main( String[] args ) {

        //Create secure connection
        SecureURLReader.connection();

        //Generate temporally users
        generateUsers();

        //Set file location
        staticFileLocation("/public");

        //Get environment port
        port(getPort());

        //API: secure(keystoreFilePath, keystorePassword, truststoreFilePath, truststorePassword);
        secure("keystores/ecikeystore.p12", "password", null, null);

        get("/hello", (req, res) -> "Hello World");

        // Allow CORS
        options("/*",
                (request, response) -> {
                    String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                    }
                    String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                    }
                    return "OK";
                });
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        get("/", (req, res) -> {
            res.redirect("/index.html");
            res.status(200);
            return null;
        });

        post("/login", (req, res) -> {
            res.type("application/json");
            if (req.body() != null) {
                ArrayList<String> responses = login(req);
                return createJson(200, responses.get(0), responses.get(1));
            }
            return createJson(400, "Bad request" ,"Not logged");
        });


        get("/login/service", (req, res) -> {
            System.out.println("SERVICIO");
            return createJson(200, "Login successful!", getSession(req));
        });

    }


    /**
     * Method that allow the log in
     * @param req, POST request sent
     * @return Array with responses
     */
    private static ArrayList<String> login(Request req){
        User user = (new Gson()).fromJson(req.body(), User.class);

        ArrayList<String> responses = new ArrayList<String>();
//        System.out.println(user.getName());
//        System.out.println(user.getPassword());
        if(users.containsKey(user.getName())){
//            System.out.println(users.get(user.getName()).equals(generateCode(user.getPassword())));
            if(users.get(user.getName()).equals(generateCode(user.getPassword()))){
                req.session(true);
                req.session().attribute("login", true);
//                System.out.println("Login successful! ");
                responses.add("Login successful!");
//                responses.add(SecureURLReader.readURL("https://localhost:2703/hello"));
                responses.add(getSession(req));
                return responses;
            }
//            System.out.println("Wrong user or password");
            responses.add("Wrong password");
//            responses.add("Not Logged");
            responses.add(getSession(req));
            return responses;
        }
//        System.out.println("User doesn't exists");
        responses.add("User doesn't exists");
//        responses.add("Not Logged");
        responses.add(getSession(req));
        return responses;
    }


    /**
     * Generate the communication with other server
     * @param req, POST request sent
     * @return returned statement from server
     */
    private static String getSession(Request req){
        req.session();
        try{
            if((boolean)req.session().attribute("login")){
//                System.out.println("getSession");
//                System.out.println("getSession: " + SecureURLReader.readURL("https://localhost:2703/hello"));
//                return SecureURLReader.readURL("https://localhost:2703/hello");

                System.out.println("getSession: " + SecureURLReader.readURL("https://ec2-54-205-27-252.compute-1.amazonaws.com:2703/hello"));
                return SecureURLReader.readURL("https://ec2-54-205-27-252.compute-1.amazonaws.com:2703/hello");
            }
        }catch(Exception e){
            System.out.println(e);
            System.out.println("Not");
            return "Not Logged in!";
        }
        return null;

    }






    //Other necessary methods

    /**
     * Generate a hashCode to encode password
     * @param password, String with the password
     * @return encoded password
     */
    private static Integer generateCode(String password){
        return password.hashCode();
    }


    /**
     * Create users from temporally database
     */
    private static  void generateUsers(){
        users.put("Laura", generateCode("password"));
        users.put("Val", generateCode("mypassword"));
    }


    /**
     * create a JSON Object to generate correct responses
     * @param status, Set HTTP status
     * @param result, Set response from login service
     * @param serverResponse, Set response from another server
     * @return
     */
    private static JsonObject createJson(int status, String result, String serverResponse){
        JsonObject json  =new JsonObject();
        json.addProperty("status", status);
        json.addProperty("result", result);
        json.addProperty("server", serverResponse);

        return json;

    }


    /**
     * This method reads the default port as specified by the PORT variable in
     * the environment.
     *
     * Heroku provides the port automatically so you need this to run the
     * project on Heroku.
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e. on localhost)
    }

    /**
     * Get default Keystore
     * @return Keystore
     */
    static String getKeyStore() {
        if (System.getenv("KEYSTORE") != null) {
            return System.getenv("KEYSTORE");
        }
        return "keystores/ecikeystore.p12"; //returns default keystore if keystore isn't set (i.e. on localhost)
    }


}







