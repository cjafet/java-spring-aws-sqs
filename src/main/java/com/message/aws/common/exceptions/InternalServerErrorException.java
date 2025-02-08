package com.message.aws.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class InternalServerErrorException extends Exception {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Erro inesperado no servidor.");
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now().toString());

        return response;
    }
}
