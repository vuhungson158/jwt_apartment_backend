package com.hung91hn.apartment.security;

import com.hung91hn.apartment.helper.Util;
import com.hung91hn.apartment.model.Response;
import com.hung91hn.apartment.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Autowired
    private Util util;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().antMatchers("/", "/register", "/active", "/login").permitAll().and()
                .authorizeRequests().antMatchers("/getUser").hasRole(User.USER).and()
                .authorizeRequests().anyRequest().authenticated().and()
                .httpBasic().authenticationEntryPoint((request, response, authException)
                -> util.writeResponse(response, Response.INVALID_TOKEN, "Phiên đăng nhập hết hạn."))
                .and().exceptionHandling().accessDeniedHandler((request, response, accessDeniedException)
                -> util.writeResponse(response, Response.INVALID_ROLE, "Không đủ quyền."));
    }
}