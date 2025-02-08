package com.message.aws.port;

public interface AuthenticationPort {
    boolean validateAuthorizationHeader(String authorizationHeader);
    Boolean validateIsTokenExpired(String authorizationHeader);
}
