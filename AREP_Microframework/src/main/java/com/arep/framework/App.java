package com.arep.framework;

public class App {
    public static void main(String[] args) throws Exception {
        MiniSpark.staticfiles("src/main/resources/webroot");

        MiniSpark.get("/hello", (req, res) -> "Hello " + req.getValues("name"));
        MiniSpark.get("/pi", (req, res) -> String.valueOf(Math.PI));

        MiniSpark.start();
    }
}
