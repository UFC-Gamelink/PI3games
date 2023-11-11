package com.gamelink.gamelinkapi.dtos.responses.communities;

public record CommunityResponse(
        String name,
        String description,
        String bannerUrl,
        String owner
) {
}
