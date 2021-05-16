package com.hung91hn.apartment.model;

public class UserLogin {
    public String token;
    public User user;

    public UserLogin(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
