package com.gamelink.gamelinkapi.utils.creators;

import com.gamelink.gamelinkapi.dtos.requests.users.UserProfileRequest;
import com.gamelink.gamelinkapi.enums.GameTime;
import com.gamelink.gamelinkapi.enums.Gender;
import com.gamelink.gamelinkapi.models.users.User;

import java.time.LocalDate;
import java.util.List;

public class UserProfileRequestCreator implements Creator<UserProfileRequest>{
    private static UserProfileRequestCreator instance;

    private UserProfileRequestCreator() {
    }

    public static UserProfileRequestCreator getInstance() {
        if (instance == null) instance = new UserProfileRequestCreator();
        return instance;
    }

    @Override
    public UserProfileRequest createValid() {
        return UserProfileRequest.builder()
                .name("user")
                .bio("minha bio")
                .birthdayDate(LocalDate.now())
                .gender(Gender.MALE)
                .user(User.builder()
                        .email("valid@email.com")
                        .password("1@aA1234")
                        .username("user")
                        .build())
                .gameTimes(List.of(GameTime.NIGHT))
                .build();
    }
}
