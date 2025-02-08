package com.message.aws.core.port;

public interface AuthenticationPort {
    boolean validateAuthorizationHeader(String authorizationHeader);
    Boolean validateIsTokenExpired(String authorizationHeader);
}
