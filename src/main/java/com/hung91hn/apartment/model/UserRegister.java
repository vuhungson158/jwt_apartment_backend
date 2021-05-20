package com.hung91hn.apartment.model;

import com.hung91hn.apartment.security.UserPrincipal;

import java.io.Serializable;

public class UserRegister implements Serializable {
    public int otp;
    public UserPrincipal user;

    public UserRegister(int otp, UserPrincipal user) {
        this.otp = otp;
        this.user = user;
    }
}
