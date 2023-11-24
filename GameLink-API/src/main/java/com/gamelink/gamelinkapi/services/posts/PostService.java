package com.gamelink.gamelinkapi.services.posts;

import com.gamelink.gamelinkapi.dtos.requests.posts.PostRequest;
import com.gamelink.gamelinkapi.dtos.responses.posts.PostResponse;
import com.gamelink.gamelinkapi.mappers.PostMapper;
import com.gamelink.gamelinkapi.models.comunities.CommunityModel;
import com.gamelink.gamelinkapi.models.images.ImageModel;
import com.gamelink.gamelinkapi.models.posts.PostModel;
import com.gamelink.gamelinkapi.models.users.User;
import com.gamelink.gamelinkapi.repositories.communities.CommunityRepository;
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
    private final CommunityRepository communityRepository;
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
                .map(postMapper::modelToResponse)
                .toList();
    }

    @Transactional
    public void delete(UUID id) {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        PostModel postFounded = postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("this post doesn't exists")
        );

        if (postFounded.getOwner().getId() == user.getId()) {
            postRepository.deleteById(id);
            imageCloudService.deleteImageOrThrowSaveThreatementException(postFounded.getImage());
        } else {
            throw new BadCredentialsException("Invalid user");
        }
    }
}
