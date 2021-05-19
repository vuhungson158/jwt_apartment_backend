package com.hung91hn.apartment.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hung91hn.apartment.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class Util {
    public void print(String s) {
        System.out.println(s);
    }

    @Autowired
    private ObjectMapper mapper;

    public void writeResponse(HttpServletResponse response, int code, String msg) throws IOException {
        mapper.writeValue(response.getWriter(), new Response(code, msg));
    }
}
