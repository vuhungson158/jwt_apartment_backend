package com.hung91hn.apartment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hung91hn.apartment.helper.Log;
import com.hung91hn.apartment.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;

@RestControllerAdvice
public class RequestAdvice extends RequestBodyAdviceAdapter {
    @Autowired
    private Log log;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        final StringBuilder builder = new StringBuilder()
                .append(request.getMethod()).append(':').append(request.getServletPath())
                .append(" id=").append(request.getSession().getId());
        final String token = request.getHeader(JwtRequestFilter.KeyAut);
        if (StringUtils.hasLength(token)) builder.append("\ntoken:").append(token);

        try {
            builder.append('\n').append(mapper.writeValueAsString(body));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.i(builder.toString());
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}
