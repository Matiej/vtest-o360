package com.vtest.o360.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestRegisterUser {
    private String userName;
    private String password;
    private String passwordMatch;

    @Override
    public String toString() {
        return "RestRegisterUser{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", passwordMatch='" + passwordMatch + '\'' +
                '}';
    }

    public RegisterUserCommand toCommand() {
        return new RegisterUserCommand(userName, password, passwordMatch);
    }


}
