package com.gamelink.gamelinkapi.dtos.requests.users;

import com.gamelink.gamelinkapi.enums.GameTime;
import com.gamelink.gamelinkapi.enums.Gender;
import com.gamelink.gamelinkapi.models.users.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Builder
public record UserProfileRequest(
        @NotBlank String name,
        @NotBlank @Size(max = 160) String bio,
        @NotNull LocalDate birthdayDate,
        List<GameTime> gameTimes,
        @NotNull Gender gender
) {
    public UserProfileRequest {
        if (gameTimes == null) gameTimes = Collections.emptyList();
    }
}
