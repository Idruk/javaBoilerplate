package com.example.springboot;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.example.springboot.utils.JwtUtils;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        if (request.getHeader("Authorization") != null) {

            String[] auth = request.getHeader("Authorization").split(" ");

            if (auth[0].equals("Bearer") && auth[1] != null) {
                if (JwtUtils.verifyJwt(auth[1])) {
                    return true;
                }
            }
        }

        response.setContentType("application/json");
        response.setStatus(401);
        response.getWriter().write("{\"message\":\"Error unAuthorized\"}");

        return false;
    }
}
