package com.vtest.o360.user;

import lombok.Builder;
import lombok.Value;

@Value
public class RegisterUserCommand {
    String userName;
    String password;
    String passwordMatch;
}
