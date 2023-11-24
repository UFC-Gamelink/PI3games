package com.gamelink.gamelinkapi.dtos.responses.posts;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private UUID id;
    private String description;
    private LocalDateTime postDate;
    private String imageUrl;
    private UUID ownerId;
    private String ownerName;
    private String username;
    private String userIconUrl;
    private boolean isLiked;
    private int likeQuantity;
}
