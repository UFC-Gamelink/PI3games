package com.gamelink.gamelinkapi.dtos.requests.posts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventPostRequest(
        @NotBlank String description,
        @NotNull double latitude,
        @NotNull double longitude,
        LocalDateTime eventDate
) {
}
