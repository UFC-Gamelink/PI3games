package com.gamelink.gamelinkapi.dtos.responses.commentaries;

import java.util.UUID;

public record CommentaryResponse(
        UUID id,
        String text,
        UUID postId,
        UUID ownerId,
        String ownerName,
        String username,
        String userIconUrl
) {
}
