package com.message.aws.core.exception;

public class RequestUnauthorized extends RuntimeException {
    public RequestUnauthorized(String message) {
        super(message);
    }
}
