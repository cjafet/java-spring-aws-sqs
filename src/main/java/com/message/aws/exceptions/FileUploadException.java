package com.message.aws.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class FileUploadException extends Exception{

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public Map<String, Object> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.PAYLOAD_TOO_LARGE.value());
        response.put("error", "O arquivo enviado é muito grande. O tamanho máximo permitido é 1GB.");
        response.put("timestamp", LocalDateTime.now().toString());

        return response;
    }
}