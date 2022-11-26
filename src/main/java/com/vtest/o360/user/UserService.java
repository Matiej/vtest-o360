package com.vtest.o360.user;

import java.util.Optional;

public interface UserService {

    RegisterUserResponse register(RegisterUserCommand command);
    Optional<UserResponse> getById(Long id);

}
