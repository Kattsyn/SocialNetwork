package kattsyn.dev.StatService.service;

import kattsyn.dev.StatService.entities.Author;
import kattsyn.dev.StatService.entities.Event;
import kattsyn.dev.StatService.entities.Like;
import kattsyn.dev.StatService.entities.Post;
import kattsyn.dev.StatService.enums.Events;
import kattsyn.dev.StatService.repositories.PostRepository;
import kattsyn.dev.StatService.services.AuthorService;
import kattsyn.dev.StatService.services.EventService;
import kattsyn.dev.StatService.services.LikeService;
import kattsyn.dev.StatService.services.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    @InjectMocks
    PostService postService;

    @Mock
    private PostRepository postRepository;
    @Mock
    private AuthorService authorService;
    @Mock
    private EventService eventService;
    @Mock
    private LikeService likeService;

    @Test
    void processEvent_FirstLike_ShouldSaveEvent() {
        long eventId = 1L;
        long postId = 1L;
        long userId = 1L;

        Event event = new Event();
        event.setId(eventId);
        event.setPostId(postId);
        event.setUserId(userId);
        event.setType(Events.EVENT_LIKE);

        Author author = new Author();
        author.setId(userId);

        Post post = new Post();
        post.setId(postId);
        post.setAuthor(author);

        when(likeService.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.empty());
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.processEvent(event);

        verify(eventService, times(1)).save(any(Event.class));
        verify(likeService, times(1)).save(any(Like.class));
        verify(postRepository, times(1)).save(any(Post.class));
        verify(authorService, times(1)).save(any(Author.class));
    }

    @Test
    void processEvent_SecondLike_ShouldNotSaveEvent() {
        long eventId = 1L;
        long postId = 1L;
        long userId = 1L;

        Event event = new Event();
        event.setId(eventId);
        event.setPostId(postId);
        event.setUserId(userId);
        event.setType(Events.EVENT_LIKE);

        when(likeService.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.of(new Like()));

        postService.processEvent(event);

        verify(eventService, never()).save(any(Event.class));
    }

    @Test
    void processEvent_Views_ShouldSaveEvents() {
        long eventId = 1L;
        long postId = 1L;
        long userId = 1L;

        Event firstEvent = new Event();
        firstEvent.setId(eventId);
        firstEvent.setPostId(postId);
        firstEvent.setUserId(userId);
        firstEvent.setType(Events.EVENT_VIEW);

        Event secondEvent = new Event();
        secondEvent.setId(eventId + 1);
        secondEvent.setPostId(postId);
        secondEvent.setUserId(userId + 1);
        secondEvent.setType(Events.EVENT_VIEW);

        Author author = new Author();
        author.setId(userId);

        Post post = new Post();
        post.setId(postId);
        post.setAuthor(author);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.processEvent(firstEvent);
        postService.processEvent(secondEvent);

        verify(eventService, times(2)).save(any(Event.class));
        verify(postRepository, times(2)).save(any(Post.class));
        verify(authorService, times(2)).save(any(Author.class));
    }


    @Test
    void getLikesByPostId_ExistingPost_ShouldReturnInt() {
        long postId = 1L;
        int likes = 123;

        Post post = new Post();
        post.setId(postId);
        post.setLikes(likes);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        assertThat(postService.getLikesByPostId(postId)).isEqualTo(likes);
    }

    @Test
    void getLikesByPostId_NotExistingPost_ShouldThrowRuntimeException() {
        Long postId = 1L;

        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getLikesByPostId(postId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("POST NOT FOUND");
    }

    @Test
    void getViewsByPostId_ExistingPost_ShouldReturnInt() {
        Long postId = 1L;
        int views = 123;

        Post post = new Post();
        post.setId(postId);
        post.setViews(views);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        assertThat(postService.getViewsByPostId(postId)).isEqualTo(views);
    }

    @Test
    void getViewsByPostId_NotExistingPost_ShouldThrowRuntimeException() {
        Long postId = 1L;

        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getViewsByPostId(postId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("POST NOT FOUND");
    }

}
