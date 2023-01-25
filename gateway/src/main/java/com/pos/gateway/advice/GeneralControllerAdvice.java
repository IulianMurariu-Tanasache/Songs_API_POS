package com.pos.gateway.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
@Slf4j
public class GeneralControllerAdvice {

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<?> handleClientErrors(WebClientResponseException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getStatusCode());
    }
}
