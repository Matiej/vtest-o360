package com.vtest.o360.user;

import lombok.Value;

@Value
public class RegisterUserResponse {
    Long id;
    String errorMessage;
    boolean isSuccess;

    public static RegisterUserResponse SUCCESS(Long id) {
        return new RegisterUserResponse(id, null, true);
    }

    public static RegisterUserResponse FAILURE(String errorMessage) {
        return new RegisterUserResponse(null, errorMessage, false);
    }
}
