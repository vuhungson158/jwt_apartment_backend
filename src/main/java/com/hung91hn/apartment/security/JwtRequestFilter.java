package com.hung91hn.apartment.security;

import com.hung91hn.apartment.Util;
import com.hung91hn.apartment.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    public static final String KeyAut = "Authorization";

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Util util;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String appVer = request.getHeader("appVer");
        if ("/".equals(request.getServletPath()) || appVer != null && appVer.equals("1.0")) {
            final UserPrincipal user = jwtUtil.validate(request.getHeader(KeyAut));
            if (user != null) {
                final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                        null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } else util.writeResponse(response, Response.INVALID_VERSION, "Sai phiên bản");
        //todo final String language = request.getHeader("language");
    }

}