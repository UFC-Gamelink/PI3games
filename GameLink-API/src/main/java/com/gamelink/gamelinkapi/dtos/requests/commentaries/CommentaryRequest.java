package com.gamelink.gamelinkapi.dtos.requests.commentaries;

import jakarta.validation.constraints.NotBlank;

public record CommentaryRequest(
        @NotBlank String text
) {
}
