package com.gamelink.gamelinkapi.models.posts.likes;

import com.gamelink.gamelinkapi.models.posts.PostModel;
import com.gamelink.gamelinkapi.models.users.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeId implements Serializable {
    @ManyToOne
    private User user;
    @ManyToOne
    private PostModel post;
}
