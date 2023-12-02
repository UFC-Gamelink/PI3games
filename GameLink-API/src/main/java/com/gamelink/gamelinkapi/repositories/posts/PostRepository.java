package com.gamelink.gamelinkapi.repositories.posts;

import com.gamelink.gamelinkapi.models.posts.PostModel;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<PostModel, UUID> {
    List<PostModel> findAllByOwnerOrderByCreatedAtDesc(UserProfile owner);
    List<PostModel> findAllByCommunityNullOrderByCreatedAtDesc();
    List<PostModel> findAllByCommunityNotNullAndOwnerOrderByCreatedAtDesc(UserProfile owner);
    int countAllByOwner(UserProfile owner);
}
