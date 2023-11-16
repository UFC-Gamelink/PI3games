package com.gamelink.gamelinkapi.dtos.requests.posts;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record PostRequest(
        @NotBlank String description,
        MultipartFile image
) {
}
