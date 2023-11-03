package com.gamelink.gamelinkapi.utils.creators;

import com.gamelink.gamelinkapi.dtos.responses.users.UserProfileResponse;
import com.gamelink.gamelinkapi.enums.GameTime;
import com.gamelink.gamelinkapi.enums.Gender;

import java.time.LocalDate;
import java.util.List;

public class UserProfileResponseCreator implements Creator<UserProfileResponse>{
    private static UserProfileResponseCreator INSTANCE;

    private UserProfileResponseCreator() {
    }

    public static UserProfileResponseCreator getInstance() {
        if (INSTANCE == null) INSTANCE = new UserProfileResponseCreator();
        return INSTANCE;
    }

    @Override
    public UserProfileResponse createValid() {
        return UserProfileResponse.builder()
                .name("user")
                .bio("minha bio")
                .birthdayDate(LocalDate.now())
                .gender(Gender.MALE)
                .gameTimes(List.of(GameTime.NIGHT))
                .build();
    }
}
