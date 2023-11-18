package com.gamelink.gamelinkapi.services.posts;

import com.gamelink.gamelinkapi.dtos.responses.posts.PostResponse;
import com.gamelink.gamelinkapi.mappers.PostMapper;
import com.gamelink.gamelinkapi.models.images.ImageModel;
import com.gamelink.gamelinkapi.models.posts.PostModel;
import com.gamelink.gamelinkapi.models.users.User;
import com.gamelink.gamelinkapi.repositories.posts.PostRepository;
import com.gamelink.gamelinkapi.services.cloudinary.ImageCloudService;
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
    private final PostMapper postMapper = PostMapper.INSTANCE;

    @Transactional
    public void save(MultipartFile image, String description) {
        User userFounded = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        ImageModel imageSaved = imageCloudService.saveImageOrThrowSaveThreatementException(image);

        PostModel postToBeSaved = PostModel.builder()
                .user(userFounded)
                .image(imageSaved)
                .description(description)
                .build();

        postRepository.save(postToBeSaved);
    }

    public List<PostResponse> findAll() {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();

        return postRepository.findAllByUser(user)
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

        if (postFounded.getUser().getId() == user.getId()) {
            postRepository.deleteById(id);
            imageCloudService.deleteImageOrThrowSaveThreatementException(postFounded.getImage());
        } else {
            throw new BadCredentialsException("Invalid user");
        }
    }
}
