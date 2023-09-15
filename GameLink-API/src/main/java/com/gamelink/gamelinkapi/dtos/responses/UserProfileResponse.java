package com.gamelink.gamelinkapi.dtos.responses;

import com.gamelink.gamelinkapi.enums.GameTime;
import com.gamelink.gamelinkapi.enums.Gender;
import com.gamelink.gamelinkapi.models.User;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UserProfileResponse(
    String name,
    User user,
    String bio,
    LocalDateTime entryDate,
    LocalDate birthdayDate,
    List<GameTime> gameTimes,
    Gender gender
) {
}
