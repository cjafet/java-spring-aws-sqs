package com.message.aws.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenPayload {
    private String sub;
    private String name;
    private Integer iat;
}
