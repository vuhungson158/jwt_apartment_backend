package com.hung91hn.apartment.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hung91hn.apartment.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserPrincipal extends User implements UserDetails {
    private static final long serialVersionUID = 1L;

    public String notifyId, deviceId, os, osVer;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final Set<GrantedAuthority> authorities = new HashSet<>();
        for (String role : checkRoles())
            authorities.add(new SimpleGrantedAuthority(User.ROLE_ + role));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return phone;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

}
