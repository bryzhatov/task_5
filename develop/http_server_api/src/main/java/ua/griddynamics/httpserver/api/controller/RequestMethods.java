package ua.griddynamics.httpserver.api.controller;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-26
 */
public enum RequestMethods {
    GET("GET"),
    POST("POST"),
    PUT("PUT");

    private String name;

    RequestMethods(String name) {
        this.name = name;
    }

    //TODO
    public static RequestMethods get(String name) {
        name = name.toUpperCase();

        switch (name) {
            case "GET":
                return GET;
            case "POST":
                return POST;
            case "PUT":
                return PUT;
            default:
//                throw new IllegalArgumentException("Method like this:" + name + " is not exists.");
                return null;
        }
    }

}
