package com.gamelink.gamelinkapi.dtos.responses.posts;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostResponse(
        UUID id,
        String description,
        LocalDateTime postDate,
        String imageUrl,
        UUID ownerId,
        String ownerName,
        String username,
        String userIconUrl
) {
}
