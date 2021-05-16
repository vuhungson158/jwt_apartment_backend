package com.hung91hn.apartment.model;

import java.io.Serializable;

public class UserRegister implements Serializable {
    public int otp;
    public User user;

    public UserRegister(int otp, User user) {
        this.otp = otp;
        this.user = user;
    }
}
