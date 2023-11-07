package com.gamelink.gamelinkapi.dtos.responses.users;

import com.gamelink.gamelinkapi.dtos.responses.images.ImageResponseDto;
import com.gamelink.gamelinkapi.enums.GameTime;
import com.gamelink.gamelinkapi.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record UserProfileResponse(
        UUID id,
        String owner,
        String name,
        String bio,
        LocalDate entryDate,
        LocalDate birthdayDate,
        List<GameTime> gameTimes,
        Gender gender,
        ImageResponseDto icon,
        ImageResponseDto banner
) {
}
