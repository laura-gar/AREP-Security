package edu.escuelaing.arep;

import static spark.Spark.*;


/**
 * Hello Server class
 * @author Laura García
 * @version 1.0
 */
public class HelloServer {

    /**
     * Main method
     * @param args
     */
    public static void main(String args []){
        secure("keystores/ecikeystore.p12", "password", null, null);
        port(getPort());
        get("/hello", (req, res) -> "Hello From My New Server");
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
        return 2703; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
