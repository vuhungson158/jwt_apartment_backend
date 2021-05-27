package com.hung91hn.apartment;

import com.hung91hn.apartment.model.Response;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Response handleAllException(Exception e, WebRequest request) {
        if (e instanceof AccessDeniedException) {
            return new Response(Response.INVALID_ROLE, "Không đủ quyền.");
        } else {
            e.printStackTrace();
            return new Response(Response.ERROR_SERVER, "Lỗi server!");
        }
    }
}
