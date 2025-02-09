package com.message.aws.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenPayloadDTO {
    private String sub;
    private String name;
    private Integer iat;
}
