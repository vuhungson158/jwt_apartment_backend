package com.hung91hn.apartment.model;

public class Response {
    public static final int SUCCESS = 200, ERROR_SERVER = 500,
            ERROR_CLIENT = 400, INVALID_TOKEN = 401, INVALID_VERSION = 402, INVALID_ROLE = 403;

    public int code;
    public String message;
    public Object data;

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(String message) {
        this.code = ERROR_CLIENT;
        this.message = message;
    }

    public Response() {
        this.code = SUCCESS;
        this.message = "Thành công";
    }

    public Response(Object data) {
        this();
        this.data = data;
    }
}
