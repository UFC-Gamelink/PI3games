package com.gamelink.gamelinkapi.dtos.requests.communities;

import jakarta.validation.constraints.NotBlank;

public record CommunityRequest(
        @NotBlank String name,
        @NotBlank String description
) {
}
