package com.hung91hn.apartment.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    //STATE_
    public static final int AVAILABLE = 1;
    //ROLE_
    public static final String regex = ",", USER = "USER", HOST = "HOST", ADMIN = "ADMIN";
    @Id
    public long id;

    public int state;

    public String phone, password, idCard, roles, displayName, avatar, notifyId, deviceId, os, osVer;

    public void addRole(String r) {
        roles += regex + r;
    }

    public String[] getRoles() {
        return roles.split(regex);
    }
}
