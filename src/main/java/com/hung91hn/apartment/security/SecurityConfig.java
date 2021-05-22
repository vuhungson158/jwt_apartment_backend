package com.hung91hn.apartment.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
//    @Autowired
//    private Util util;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().anyRequest().permitAll()/*.and()
                .authorizeRequests().antMatchers("/", "/register", "/active", "/login", "/place/gets", "place/pictures/download").permitAll().and()
                .authorizeRequests().antMatchers("/place/create").hasRole(User.USER).and()
                .httpBasic().authenticationEntryPoint((request, response, authException)
                -> util.writeResponse(response, Response.INVALID_TOKEN, "Phiên đăng nhập hết hạn."))
                .and().exceptionHandling().accessDeniedHandler((request, response, accessDeniedException)
                -> util.writeResponse(response, Response.INVALID_ROLE, "Không đủ quyền."))*/;
    }
}