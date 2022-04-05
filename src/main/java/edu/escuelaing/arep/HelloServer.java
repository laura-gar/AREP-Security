package edu.escuelaing.arep;

import static spark.Spark.*;

public class HelloServer {


    public static void main(String args []){
        secure("keystores/ecikeystore.p12", "password", null, null);
        port(getPort());
        get("/hello", (req, res) -> "Hello From My New Server");
    }


    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 2703; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
