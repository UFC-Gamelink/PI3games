package com.gamelink.gamelinkapi.dtos.responses.communities;

import com.gamelink.gamelinkapi.dtos.responses.posts.PostResponse;

import java.util.List;
import java.util.UUID;

public record CommunityResponse(
        UUID id,
        String name,
        String description,
        String bannerUrl,
        String owner,
        UUID ownerId,
        List<PostResponse> posts
) {
}
