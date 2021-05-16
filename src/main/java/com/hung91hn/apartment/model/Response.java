package com.hung91hn.apartment.model;

public class Response {
    public int code;
    public String message;

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(String message) {
        this.code = 400;
        this.message = message;
    }
}
