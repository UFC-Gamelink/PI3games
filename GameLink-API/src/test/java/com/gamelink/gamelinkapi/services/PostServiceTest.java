package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.dtos.responses.posts.PostResponse;
import com.gamelink.gamelinkapi.models.posts.PostModel;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import com.gamelink.gamelinkapi.repositories.posts.PostRepository;
import com.gamelink.gamelinkapi.services.posts.PostService;
import com.gamelink.gamelinkapi.services.users.UserProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class PostServiceTest {
    @Autowired
    private PostService service;
    @MockBean
    private PostRepository repository;
    @MockBean
    private UserProfileService userProfileService;

    @Test
    @DisplayName("Post should execute successfully when a user is authenticated and the values are valid")
    void postSuccess() {
        final var userProfile = new UserProfile();
        final var text = "post text";
        final var postId = UUID.randomUUID();

        when(userProfileService.findUserProfileByContext()).thenReturn(userProfile);
        when(repository.save(any())).then(
                (value) -> {
                    PostModel post = value.getArgument(0);
                    post.setId(postId);
                    return post;
                }
        );

        var postIdSaved = service.save(null, text);

        verify(userProfileService, times(1)).findUserProfileByContext();
        verify(repository, times(1)).save(any());

        assertNotNull(postIdSaved);
        assertEquals(postId, postIdSaved);
    }

    @Test
    @DisplayName("findAll should execute successfully when a user is authenticated and the values are valid")
    void findAllSuccess() {
        final var userProfile = new UserProfile();

        when(userProfileService.findUserProfileByContext()).thenReturn(userProfile);
        when(repository.findAllByOwnerOrderByCreatedAtDesc(userProfile)).thenReturn(List.of());

        List<PostResponse> response = service.findAll();
        verify(userProfileService, times(1)).findUserProfileByContext();
        verify(repository, times(1)).findAllByOwnerOrderByCreatedAtDesc(userProfile);

        assertNotNull(response);
        assertEquals(0, response.size());
    }
}
