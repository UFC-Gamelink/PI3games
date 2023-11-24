package com.gamelink.gamelinkapi.repositories.posts;

import com.gamelink.gamelinkapi.models.posts.PostModel;
import com.gamelink.gamelinkapi.models.posts.likes.LikeId;
import com.gamelink.gamelinkapi.models.posts.likes.LikeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<LikeModel, LikeId> {
    int countById_Post_Id(UUID id_post_id);
}
