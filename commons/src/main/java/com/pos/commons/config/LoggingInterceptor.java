package com.pos.commons.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Log request details here
        log.info("Request URL: " + request.getRequestURL().toString());
        log.info("Request Method: " + request.getMethod());
        log.info("Request Authorization: " + request.getHeader("Authorization"));
        log.info("Request IP: " + request.getRemoteAddr());
        return true;
    }
}
