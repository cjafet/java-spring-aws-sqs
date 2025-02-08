package com.message.aws.exceptions;

public class SNSPublishingException extends RuntimeException {
    public SNSPublishingException(String message, Throwable cause) {
        super(message, cause);
    }
}