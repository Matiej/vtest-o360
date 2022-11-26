package com.vtest.o360.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;

    public static UserResponse toResponse(UserEntity user) {
        return UserResponse.builder().id(user.getId()).username(user.getUsername()).build();
    }
}
