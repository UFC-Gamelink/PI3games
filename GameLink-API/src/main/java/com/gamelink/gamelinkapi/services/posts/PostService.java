package com.gamelink.gamelinkapi.services.posts;

import com.gamelink.gamelinkapi.dtos.responses.posts.PostResponse;
import com.gamelink.gamelinkapi.mappers.PostMapper;
import com.gamelink.gamelinkapi.models.images.ImageModel;
import com.gamelink.gamelinkapi.models.posts.PostModel;
import com.gamelink.gamelinkapi.models.posts.likes.LikeId;
import com.gamelink.gamelinkapi.models.posts.likes.LikeModel;
import com.gamelink.gamelinkapi.models.users.User;
import com.gamelink.gamelinkapi.repositories.posts.LikeRepository;
import com.gamelink.gamelinkapi.repositories.posts.PostRepository;
import com.gamelink.gamelinkapi.services.cloudinary.ImageCloudService;
import com.gamelink.gamelinkapi.services.users.UserProfileService;
import com.gamelink.gamelinkapi.services.users.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final ImageCloudService imageCloudService;
    private final UserProfileService userProfileService;
    private final LikeRepository likeRepository;
    private final PostMapper postMapper = PostMapper.INSTANCE;

    @Transactional
    public UUID save(MultipartFile image, String description) {
        var userProfileFounded = userProfileService.findUserProfileByContext();

        PostModel postToBeSaved = PostModel.builder()
                .owner(userProfileFounded)
                .description(description)
                .build();

        if (image != null) {
            ImageModel imageSaved = imageCloudService.saveImageOrThrowSaveThreatementException(image);
            postToBeSaved.setImage(imageSaved);
        }

        return postRepository.save(postToBeSaved).getId();
    }

    public List<PostResponse> findAll() {
        var userProfileFound = userProfileService.findUserProfileByContext();

        return postRepository.findAllByOwner(userProfileFound)
                .stream()
                .map(post -> {
                    PostResponse postResponse = postMapper.modelToResponse(post);
                    postResponse.setLiked(postIsLikedByThisUser(buildLikeId(post.getId())));
                    postResponse.setLikeQuantity(likeRepository.countById_Post_Id(post.getId()));
                    return postResponse;
                })
                .toList();
    }

    @Transactional
    public void delete(UUID id) {
        var user = userProfileService.findUserProfileByContext();
        PostModel postFounded = findPostOrThrowsEntityNotFoundException(id);

        if (postFounded.getOwner().getId() == user.getId()) {
            postRepository.deleteById(id);
            if (postFounded.getImage() != null) imageCloudService.deleteImageOrThrowSaveThreatementException(postFounded.getImage());
        } else {
            throw new BadCredentialsException("Invalid user");
        }
    }

    @Transactional
    public boolean changeLike(UUID postId) {
        var likeId = buildLikeId(postId);
        boolean postIsLiked = postIsLikedByThisUser(likeId);

        if (postIsLiked) {
            likeRepository.deleteById(likeId);
        } else {
            likeRepository.save(new LikeModel(likeId));
        }

        return !postIsLiked;
    }

    private LikeId buildLikeId(UUID postId) {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        PostModel postFound = findPostOrThrowsEntityNotFoundException(postId);
        return new LikeId(user, postFound);
    }

    private boolean postIsLikedByThisUser(LikeId likeId) {
        return likeRepository.findById(likeId).isPresent();
    }

    private PostModel findPostOrThrowsEntityNotFoundException(UUID id) {
        return postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("this post doesn't exists")
        );
    }
}
