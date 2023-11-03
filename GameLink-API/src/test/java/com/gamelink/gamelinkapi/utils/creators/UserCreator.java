package com.gamelink.gamelinkapi.utils.creators;

import com.gamelink.gamelinkapi.models.users.User;

import java.util.UUID;

public class UserCreator implements Creator<User>{
    private static UserCreator INSTANCE;

    private UserCreator(){}

    public static UserCreator getInstance() {
        if (INSTANCE == null) INSTANCE = new UserCreator();
        return INSTANCE;
    }

    @Override
    public User createValid() {
        return User.builder()
                .id(UUID.randomUUID())
                .username("username")
                .email("valid@email.com")
                .password("@Aa1abcd")
                .build();
    }
}
