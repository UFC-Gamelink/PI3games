package com.gamelink.gamelinkapi.utils.creators;

import com.gamelink.gamelinkapi.dtos.responses.posts.PostResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public class PostCreator implements Creator<PostResponse>{
    private static PostCreator INSTANCE;
    private PostCreator(){}

    public static PostCreator getInstance() {
        if (INSTANCE == null) INSTANCE = new PostCreator();
        return INSTANCE;
    }

    @Override
    public PostResponse createValid() {
        return new PostResponse(UUID.randomUUID(), "tweet", LocalDateTime.now(), "url", UUID.randomUUID(), "owner", "owner", "", true, 2);
    }
}
