package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.dtos.requests.commentaries.CommentaryRequest;
import com.gamelink.gamelinkapi.dtos.responses.commentaries.CommentaryResponse;
import com.gamelink.gamelinkapi.mappers.CommentaryMapper;
import com.gamelink.gamelinkapi.models.CommentaryModel;
import com.gamelink.gamelinkapi.models.posts.PostModel;
import com.gamelink.gamelinkapi.models.user.UserProfile;
import com.gamelink.gamelinkapi.repositories.commentaries.CommentaryRepository;
import com.gamelink.gamelinkapi.repositories.posts.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentaryService {
    private final CommentaryRepository commentaryRepository;
    private final PostRepository postRepository;
    private final UserProfileService userProfileService;
    private static final CommentaryMapper commentaryMapper = CommentaryMapper.INSTANCE;

    public void save(CommentaryRequest commentaryRequest, UUID postId) {
        UserProfile userProfileFounded = userProfileService.findUserProfileByContext();
        PostModel postFound = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not exists"));

        CommentaryModel commentaryToBeSaved = commentaryMapper.requestToModel(commentaryRequest);
        commentaryToBeSaved.setCreator(userProfileFounded);
        commentaryToBeSaved.setPost(postFound);

        commentaryRepository.save(commentaryToBeSaved);
    }

    public List<CommentaryResponse> findCommentariesByPostId(UUID postId) {
        PostModel postFound = postRepository.findById(postId).orElseThrow(() ->
                new EntityNotFoundException("Post not exists"));

        return commentaryRepository.findAllByPost(postFound)
                .stream()
                .map(commentaryMapper::modelToResponse)
                .toList();
    }

    public void delete(UUID commentaryId) {
        UserProfile userProfileFounded = userProfileService.findUserProfileByContext();
        CommentaryModel commentaryFound = commentaryRepository.findById(commentaryId).orElseThrow(
                () -> new EntityNotFoundException("Commentary not found")
        );

        if (userProfileFounded.getId().equals(commentaryFound.getCreator().getId())) {
            commentaryRepository.delete(commentaryFound);
        } else {
            throw new BadCredentialsException("You can't delete this commentary");
        }
    }
}
