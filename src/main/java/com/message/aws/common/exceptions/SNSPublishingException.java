package com.message.aws.common.exceptions;

public class SNSPublishingException extends RuntimeException {
    public SNSPublishingException(String message, Throwable cause) {
        super(message, cause);
    }
}