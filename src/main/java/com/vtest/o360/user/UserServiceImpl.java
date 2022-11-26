package com.vtest.o360.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private Map<Long, UserEntity> userTempDataBase = new HashMap<>();

    @Override
    public RegisterUserResponse register(RegisterUserCommand command) {
        //Logging
        log.info("Start registering: " + command);
        //plain tex pass to db
        UserEntity user = UserEntity.builder()
                .username(command.getUserName())
                .password(command.getPassword())
                .passwordMach(command.getPasswordMatch())
                .build();
        try {
            UserEntity savedUser = saveUser(user);
            log.info("User saved successful:  " + savedUser);
            return RegisterUserResponse.SUCCESS(savedUser.getId());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return RegisterUserResponse.FAILURE("Error saving user: " + command.getUserName());
    }

    @Override
    public Optional<UserResponse> getById(Long id) {
        UserEntity userEntity = userTempDataBase.get(id);
        return Optional.ofNullable(userEntity).map(UserResponse::toResponse);
    }

    private UserEntity saveUser(UserEntity user) {
        Long id = userTempDataBase.size() + 1L;
        user.setId(id);
        userTempDataBase.put(id, user);
        return user;
    }
}
