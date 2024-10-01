package kattsyn.dev.PostService.services;

import kattsyn.dev.PostService.dtos.CreatePostRequestDTO;
import kattsyn.dev.PostService.dtos.DeletePostRequestDTO;
import kattsyn.dev.PostService.dtos.EditPostRequestDTO;
import kattsyn.dev.PostService.entities.Post;
import kattsyn.dev.PostService.exceptions.PostServiceException;
import kattsyn.dev.PostService.repositories.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceMainTest {

    @InjectMocks
    PostServiceMain postServiceMain;
    @Mock
    PostRepository postRepository;

    @Test
    void getPostById_validId_shouldReturnPost() {
        Long postId = 1L;
        String header = "testHeader";

        Post post = new Post();
        post.setPostId(postId);
        post.setHeader(header);

        when(postRepository.findByPostId(postId)).thenReturn(Optional.of(post));

        assertThat(postServiceMain.getPostById(postId)).isEqualTo(post);

        verify(postRepository, times(1)).findByPostId(postId);
    }

    @Test
    void getPostById_invalidId_shouldThrowPostServiceException() {
        Long postId = 1L;

        when(postRepository.findByPostId(postId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postServiceMain.getPostById(postId))
                .isInstanceOf(PostServiceException.class)
                .hasMessageContaining("POST NOT FOUND");

        verify(postRepository, times(1)).findByPostId(postId);
    }

    @Test
    void getPostListByIdList_validIdList_shouldReturnPostList() {
        List<Long> idList = new ArrayList<>();
        idList.add(1L);
        idList.add(2L);

        Post post1 = new Post();
        post1.setPostId(1L);
        post1.setHeader("first_post_header");

        Post post2 = new Post();
        post2.setPostId(2L);
        post2.setHeader("second_post_header");

        List<Post> expectedList = new ArrayList<>();
        expectedList.add(post1);
        expectedList.add(post2);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
        when(postRepository.findById(2L)).thenReturn(Optional.of(post2));

        List<Post> resultList = postServiceMain.getPostListByIdList(idList);

        assertThat(resultList).isEqualTo(expectedList);

        verify(postRepository, times(2)).findById(any());
    }

    @Test
    void getPostListByIdList_invalidIdList_shouldReturnNullPostList() {
        List<Long> idList = new ArrayList<>();
        idList.add(1L);
        idList.add(2L);


        List<Post> expectedList = new ArrayList<>();
        expectedList.add(null);
        expectedList.add(null);

        when(postRepository.findById(1L)).thenReturn(Optional.empty());
        when(postRepository.findById(2L)).thenReturn(Optional.empty());

        List<Post> resultList = postServiceMain.getPostListByIdList(idList);

        assertThat(resultList).isEqualTo(expectedList);

        verify(postRepository, times(2)).findById(any());
    }

    @Test
    void editPost_validRequest_shouldReturnPost() {
        Long postId = 1L;
        Long authorId = 1L;
        String header = "testHeader";
        String content = "testContent";

        EditPostRequestDTO requestDTO = new EditPostRequestDTO(postId, authorId, header, content);

        Optional<Post> postById = Optional.of(new Post());
        postById.get().setPostId(postId);
        postById.get().setAuthorId(authorId);

        Post expectedPost = new Post();
        expectedPost.setPostId(postId);
        expectedPost.setAuthorId(authorId);
        expectedPost.setHeader(header);
        expectedPost.setPostContent(content);

        when(postRepository.findByPostId(requestDTO.getPostId())).thenReturn(postById);
        when(postRepository.save(postById.get())).thenReturn(postById.get());

        assertThat(postServiceMain.editPost(requestDTO)).isEqualTo(expectedPost);

        verify(postRepository, times(1)).save(any());
    }

    @Test
    void editPost_notExistingPost_shouldThrowPostServiceException() {
        EditPostRequestDTO requestDTO = new EditPostRequestDTO();

        when(postRepository.findByPostId(requestDTO.getPostId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postServiceMain.editPost(requestDTO))
                .isInstanceOf(PostServiceException.class)
                .hasMessageContaining("POST NOT FOUND");

        verify(postRepository, never()).save(any());
    }

    @Test
    void editPost_invalidAuthorId_shouldThrowPostServiceException() {
        Long postId = 1L;
        Long requestAuthorId = 1L;
        Long postAuthorId = 2L;
        String header = "testHeader";
        String content = "testContent";

        EditPostRequestDTO requestDTO = new EditPostRequestDTO(postId, requestAuthorId, header, content);

        Optional<Post> postById = Optional.of(new Post());
        postById.get().setPostId(1L);
        postById.get().setAuthorId(postAuthorId);

        when(postRepository.findByPostId(requestDTO.getPostId())).thenReturn(postById);

        assertThatThrownBy(() -> postServiceMain.editPost(requestDTO))
                .isInstanceOf(PostServiceException.class)
                .hasMessageContaining("POST NOT FOUND");

        verify(postRepository, never()).save(any());
    }

    @Test
    void deletePost_validRequest_shouldDeletePost() {
        Long postId = 1L;
        Long authorId = 1L;

        DeletePostRequestDTO requestDTO = new DeletePostRequestDTO(postId, authorId);

        Optional<Post> postById = Optional.of(new Post());
        postById.get().setPostId(postId);
        postById.get().setAuthorId(authorId);

        when(postRepository.findByPostId(requestDTO.getPostId())).thenReturn(postById);

        postServiceMain.deletePost(requestDTO);

        verify(postRepository, times(1)).deleteById(any());
    }

    @Test
    void deletePost_notExistingPost_shouldThrowPostServiceException() {
        DeletePostRequestDTO requestDTO = new DeletePostRequestDTO(1L, 1L);

        when(postRepository.findByPostId(requestDTO.getPostId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postServiceMain.deletePost(requestDTO))
                .isInstanceOf(PostServiceException.class)
                .hasMessageContaining("POST NOT FOUND");

        verify(postRepository, never()).save(any());
    }

    @Test
    void deletePost_invalidAuthorId_shouldThrowPostServiceException() {
        Long postId = 1L;
        Long requestAuthorId = 1L;
        Long postAuthorId = 2L;

        DeletePostRequestDTO requestDTO = new DeletePostRequestDTO(postId, requestAuthorId);

        Optional<Post> postById = Optional.of(new Post());
        postById.get().setPostId(1L);
        postById.get().setAuthorId(postAuthorId);

        when(postRepository.findByPostId(requestDTO.getPostId())).thenReturn(postById);

        assertThatThrownBy(() -> postServiceMain.deletePost(requestDTO))
                .isInstanceOf(PostServiceException.class)
                .hasMessageContaining("POST NOT FOUND");

        verify(postRepository, never()).save(any());
    }

    @Test
    void createPost_validRequest_shouldSavePost() {
        Long authorId = 1L;
        String header = "testHeader";
        String content = "testContent";

        CreatePostRequestDTO requestDTO = new CreatePostRequestDTO(authorId, header, content);

        Post savedPost = new Post();
        savedPost.setAuthorId(authorId);
        savedPost.setHeader(header);
        savedPost.setPostContent(content);

        Post expectedPost = new Post();
        expectedPost.setAuthorId(authorId);
        expectedPost.setHeader(header);
        expectedPost.setPostContent(content);

        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        assertThat(postServiceMain.createPost(requestDTO)).isEqualTo(expectedPost);

        verify(postRepository, times(1)).save(any(Post.class));
    }
}
