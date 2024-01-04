package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.mappers.CommentaryMapper;
import com.gamelink.gamelinkapi.repositories.commentaries.CommentaryRepository;
import com.gamelink.gamelinkapi.repositories.posts.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CommentaryServiceTest {
    @Autowired
    private CommentaryService service;
    @MockBean
    private CommentaryRepository repository;
    @MockBean
    private UserProfileService userProfileService;
    @MockBean
    private PostRepository postRepository;
    private CommentaryMapper commentaryMapper = CommentaryMapper.INSTANCE;

//    @Test
//    @DisplayName("Post should execute successfully when a user is authenticated and the values are valid")
//    void postSuccess() {
//        final var postId = UUID.randomUUID();
//        final var userProfile = new UserProfile();
//        final var post = new PostModel();
//        final var commentary = new CommentaryRequest("commentary");
//
//        when(userProfileService.findUserProfileByContext()).thenReturn(userProfile);
//        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
//
//        service.save(commentary, postId);
//        verify(userProfileService, times(1)).findUserProfileByContext();
//        verify(postRepository, times(1)).findById(postId);
//        verify(repository, times(1)).save(ArgumentMatchers.any());
//    }
//
//    @Test
//    @DisplayName("findCommentariesByPostId should return a list of commentaries relational to the post when success")
//    void findCommentariesByPostIdSuccess() {
//        final var postId = UUID.randomUUID();
//        final var post = new PostModel();
//        post.setId(postId);
//
//        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
//        when(repository.findAllByPost(post)).thenReturn(List.of());
//
//        List<CommentaryResponse> postsFounded = service.findCommentariesByPostId(postId);
//
//        verify(postRepository, times(1)).findById(postId);
//        verify(repository, times(1)).findAllByPost(post);
//        assertNotNull(postsFounded);
//        assertEquals(0, postsFounded.size());
//    }
//
//    @Test
//    @DisplayName("delete should execute delete in CommentaryRepository when success")
//    void deleteSuccess(){
//        final var commentaryId = UUID.randomUUID();
//        final var userId = UUID.randomUUID();
//        final var userProfile = new UserProfile();
//        final var commentary = new CommentaryModel();
//
//        userProfile.setId(userId);
//        commentary.setCreator(userProfile);
//
//        when(userProfileService.findUserProfileByContext()).thenReturn(userProfile);
//        when(repository.findById(commentaryId)).thenReturn(Optional.of(commentary));
//
//        service.delete(commentaryId);
//
//        verify(userProfileService, times(1)).findUserProfileByContext();
//        verify(repository, times(1)).findById(commentaryId);
//        verify(repository, times(1)).delete(commentary);
//    }

}
