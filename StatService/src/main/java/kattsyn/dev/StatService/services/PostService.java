package kattsyn.dev.StatService.services;

import kattsyn.dev.StatService.entities.Author;
import kattsyn.dev.StatService.entities.Event;
import kattsyn.dev.StatService.entities.Post;
import kattsyn.dev.StatService.enums.Events;
import kattsyn.dev.StatService.repositories.PostRepository;
import kattsyn.dev.grpc.GetAuthorByPostIdRequest;
import kattsyn.dev.grpc.PostServiceGrpc;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostServiceGrpc.PostServiceBlockingStub postServiceBlockingStub;
    private final AuthorService authorService;
    private final EventService eventService;

    public void processEvent(Event event) {
        if (addEventAndCheckIfLikedAlready(event)) {
            return;
        }
        updatePost(event);
    }

    private boolean addEventAndCheckIfLikedAlready(Event event) {
        log.info("Зашел в метод hasUserLikedPostAlready");
        if (event.getType().equals(Events.EVENT_LIKE)) {
            Optional<Event> eventOptional = eventService.findByPostIdAndUserIdAndType(event.getPostId(), event.getUserId(), event.getType());
            if (eventOptional.isPresent()) {
                log.info("Пользователь с id: {} лайк уже ставил.", event.getUserId());
                return true;
            }
        }
        eventService.save(event);
        return false;
    }

    private void updatePost(Event event) {
        log.info("Зашел в метод updatePost");
        Optional<Post> post = postRepository.findById(event.getPostId());

        log.info("Event: {}", event);

        if (post.isPresent()) {
            Author author = post.get().getAuthor();
            switch (event.getType()) {
                case EVENT_LIKE:
                    post.get().increaseLikes(1);
                    author.increaseLikes(1);
                    break;

                case EVENT_VIEW:
                    post.get().increaseViews(1);
                    author.increaseViews(1);
                    break;
            }
            postRepository.save(post.get());
            authorService.save(author);
        } else {
            createPost(event);
            updatePost(event);
        }
    }

    private void createPost(Event event) {
        Long authorId = postServiceBlockingStub.getAuthorId(
                GetAuthorByPostIdRequest.newBuilder().setPostId(event.getPostId()).build()
        ).next().getAuthorId();
        Author author = new Author(authorId);
        Post newPost = new Post(event.getPostId(), 0, 0, author);
        authorService.save(author);
        postRepository.save(newPost);
    }

    public List<Post> findNBestPostsByViews(int N) {
        Page<Post> page = postRepository.findAll(PageRequest.of(0, N, Sort.by("views").descending()));
        return page.getContent();
    }

    public List<Post> findNBestPostsByLikes(int N) {
        Page<Post> page = postRepository.findAll(PageRequest.of(0, N, Sort.by("likes").descending()));
        return page.getContent();
    }

    public int getLikesByPostId(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            return post.get().getLikes();
        } else {
            throw new RuntimeException("POST NOT FOUND");
        }
    }

    public int getViewsByPostId(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            return post.get().getViews();
        } else {
            throw new RuntimeException("POST NOT FOUND");
        }
    }
}
