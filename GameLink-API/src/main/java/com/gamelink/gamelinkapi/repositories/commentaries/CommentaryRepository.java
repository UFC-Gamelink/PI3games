package com.gamelink.gamelinkapi.repositories.commentaries;

import com.gamelink.gamelinkapi.models.commentaries.CommentaryModel;
import com.gamelink.gamelinkapi.models.posts.PostModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentaryRepository extends CrudRepository<CommentaryModel, UUID> {
    List<CommentaryModel> findAllByPost(PostModel postModel);
}
