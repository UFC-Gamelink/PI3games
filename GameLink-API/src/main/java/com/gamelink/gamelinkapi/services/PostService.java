package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.dtos.requests.posts.EventPostRequest;
import com.gamelink.gamelinkapi.dtos.requests.posts.PostRequest;
import com.gamelink.gamelinkapi.dtos.responses.posts.PostResponse;
import com.gamelink.gamelinkapi.mappers.PostMapper;
import com.gamelink.gamelinkapi.models.CommunityModel;
import com.gamelink.gamelinkapi.models.ImageModel;
import com.gamelink.gamelinkapi.models.posts.EventPostModel;
import com.gamelink.gamelinkapi.models.posts.PostModel;
import com.gamelink.gamelinkapi.models.posts.likes.LikeId;
import com.gamelink.gamelinkapi.models.posts.likes.LikeModel;
import com.gamelink.gamelinkapi.models.user.User;
import com.gamelink.gamelinkapi.models.user.UserProfile;
import com.gamelink.gamelinkapi.repositories.communities.CommunityRepository;
import com.gamelink.gamelinkapi.repositories.posts.LikeRepository;
import com.gamelink.gamelinkapi.repositories.posts.PostRepository;
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
    private final CommunityRepository communityRepository;
    private static final PostMapper postMapper = PostMapper.INSTANCE;

    @Transactional
    public UUID save(MultipartFile image, String description) {
        var userProfileFounded = userProfileService.findUserProfileByContext();

        PostModel postToBeSaved = new PostModel();
        postToBeSaved.setOwner(userProfileFounded);
        postToBeSaved.setDescription(description);

        if (image != null) {
            ImageModel imageSaved = imageCloudService.saveImageOrThrowSaveThreatementException(image);
            postToBeSaved.setImage(imageSaved);
        }

        return postRepository.save(postToBeSaved).getId();
    }

    public List<PostResponse> findAll() {
        var userProfileFound = userProfileService.findUserProfileByContext();

        return postRepository.findAllByOwnerOrderByCreatedAtDesc(userProfileFound)
                .stream()
                .map(this::prepareResponse)
                .toList();
    }

    @Transactional
    public void delete(UUID id) {
        var user = userProfileService.findUserProfileByContext();
        PostModel postFounded = findPostOrThrowsEntityNotFoundException(id);

        if (postFounded.getOwner().getId() == user.getId()) {
            postRepository.deleteById(id);
            if (postFounded.getImage() != null)
                imageCloudService.deleteImageOrThrowSaveThreatementException(postFounded.getImage());
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

    public void saveEventPost(EventPostRequest eventPostRequest) {
        var userProfileFounded = userProfileService.findUserProfileByContext();

        var eventPostToBeSaved = postMapper.requestToEventModel(eventPostRequest);
        eventPostToBeSaved.setOwner(userProfileFounded);
        postRepository.save(eventPostToBeSaved);
    }

    public void saveCommunityPost(UUID communityId, PostRequest postRequest) {
        var userProfileFounded = userProfileService.findUserProfileByContext();
        var postToBeSaved = postMapper.requestToModel(postRequest);

        CommunityModel communityFound = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("This community not Exists"));

        if (communityFound.getMembers().contains(userProfileFounded.getUser()) || communityFound.getOwner().equals(userProfileFounded.getUser())) {
            postToBeSaved.setOwner(userProfileFounded);
            postToBeSaved.setCommunity(communityFound);

            if (postRequest.image() != null) {
                ImageModel imageSaved = imageCloudService.saveImageOrThrowSaveThreatementException(postRequest.image());
                postToBeSaved.setImage(imageSaved);
            }

            postRepository.save(postToBeSaved);
        } else {
            throw new BadCredentialsException("You can't add posts to this community");
        }

    }

    public List<PostResponse> findRecommended() {
        return postRepository.findAllByCommunityNullOrderByCreatedAtDesc()
                .stream()
                .map(this::prepareResponse)
                .toList();
    }


    public PostResponse prepareResponse(PostModel post) {
        PostResponse postResponse;

        if (post instanceof EventPostModel eventPostModel) {
            postResponse = postMapper.modelToEventResponse(eventPostModel);
        } else {
            postResponse = postMapper.modelToResponse(post);
        }

        postResponse.setLiked(postIsLikedByThisUser(buildLikeId(post.getId())));
        postResponse.setLikeQuantity(likeRepository.countById_Post_Id(post.getId()));
        return postResponse;
    }

    public List<PostResponse> findCommunitiesPosts() {
        UserProfile owner = userProfileService.findUserProfileByContext();

        return postRepository.findAllByCommunityNotNullAndOwnerOrderByCreatedAtDesc(owner)
                .stream()
                .map(this::prepareResponse)
                .toList();

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
