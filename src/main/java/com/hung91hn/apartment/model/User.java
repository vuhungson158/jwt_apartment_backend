package com.hung91hn.apartment.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum State {Disable, Activate}

    public static final String ROLE_ = "ROLE_", regex = ",", USER = "USER", HOST = "HOST", ADMIN = "ADMIN";

    @Id
    @GeneratedValue
    public long id;

    @Enumerated(EnumType.STRING)
    public State state;

    public String phone, password, roles, idCard, displayName;

    public void addRole(String r) {
        roles += regex + r;
    }

    public String[] checkRoles() {
        return roles.split(regex);
    }
}