package com.message.aws.util;

import com.message.aws.core.model.entity.UserEntity;

import java.util.Collections;

public class TestUtil {

    public static UserEntity getUserEntity(Long id, String userName, String email) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(Long.valueOf(1));
        userEntity.setUsername("cjafet");
        userEntity.setEmail("example@test.com");
        userEntity.setAuthorities(Collections.EMPTY_LIST);

        return userEntity;
    }
}
