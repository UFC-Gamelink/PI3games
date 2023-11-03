package com.gamelink.gamelinkapi.dtos.requests.users;

import com.gamelink.gamelinkapi.enums.GameTime;
import com.gamelink.gamelinkapi.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Builder
public record PutUserProfileRequest(
        @NotNull UUID id,
        @NotBlank String owner,
        @NotBlank String name,
        @NotBlank @Size(max = 160) String bio,
        @NotNull LocalDate birthdayDate,
        List<GameTime> gameTimes,
        @NotNull Gender gender
)  {
    public PutUserProfileRequest {
        if (gameTimes == null) gameTimes = Collections.emptyList();
    }
}
