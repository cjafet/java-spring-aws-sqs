package com.message.aws.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class RequestUnauthorizedException extends Exception{

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleUnauthorizedException(ResponseStatusException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode: ", HttpStatus.UNAUTHORIZED.value());
        response.put("erro: ", ex.getReason() != null ? ex.getReason() : "Token não fornecido ou inválido.");
        response.put("timestamp: ", LocalDateTime.now().toString());

        return response;
    }


}
