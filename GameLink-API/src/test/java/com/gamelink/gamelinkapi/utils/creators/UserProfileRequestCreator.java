package com.gamelink.gamelinkapi.utils.creators;

import com.gamelink.gamelinkapi.dtos.requests.users.UserProfileRequest;
import com.gamelink.gamelinkapi.enums.GameTime;
import com.gamelink.gamelinkapi.enums.Gender;

import java.time.LocalDate;
import java.util.List;


public class UserProfileRequestCreator implements Creator<UserProfileRequest>{
    private static UserProfileRequestCreator INSTANCE;

    private UserProfileRequestCreator() {
    }

    public static UserProfileRequestCreator getInstance() {
        if (INSTANCE == null) INSTANCE = new UserProfileRequestCreator();
        return INSTANCE;
    }

    @Override
    public UserProfileRequest createValid() {
        return UserProfileRequest.builder()
                .name("user")
                .bio("minha bio")
                .birthdayDate(LocalDate.now())
                .gender(Gender.MALE)
                .gameTimes(List.of(GameTime.NIGHT))
                .build();
    }
}
