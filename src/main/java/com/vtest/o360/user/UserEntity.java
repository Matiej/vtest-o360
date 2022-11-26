package com.vtest.o360.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntity {
    private Long id;
    private String username;
    private String password;
    private String passwordMach;

    //toString with password!!
    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", passwordMach='" + passwordMach + '\'' +
                '}';
    }
}
