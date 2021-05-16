package com.hung91hn.apartment.model;

public class ResponseT<T> extends Response {
    public T data;

    public ResponseT(T data) {
        super(200, "Thành công");
        this.data = data;
    }
}
