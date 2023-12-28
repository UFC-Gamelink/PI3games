package com.gamelink.gamelinkapi.dtos.requests.users;

import com.gamelink.gamelinkapi.enums.GameTime;
import com.gamelink.gamelinkapi.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Builder
public record PostUserProfileRequest(
        @NotBlank String name,
        String bio,
        @NotNull LocalDate birthdayDate,
        List<GameTime> gameTimes,
        @NotNull Gender gender,
        double latitude,
        double longitude,
        boolean showBirthday,
        boolean showLocation
) {
    public PostUserProfileRequest {
        if (gameTimes == null) gameTimes = Collections.emptyList();
        if (bio == null) bio = "";
    }
}
