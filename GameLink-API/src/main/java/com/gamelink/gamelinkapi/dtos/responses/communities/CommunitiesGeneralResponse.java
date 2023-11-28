package com.gamelink.gamelinkapi.dtos.responses.communities;

import java.util.UUID;

public record CommunitiesGeneralResponse(
        UUID id,
        String name,
        String description,
        String bannerUrl,
        String owner,
        UUID ownerId,
        int qntMembers
) {
}
