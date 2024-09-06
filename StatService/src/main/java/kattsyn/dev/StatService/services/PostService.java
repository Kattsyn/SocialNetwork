package kattsyn.dev.StatService.services;

import kattsyn.dev.StatService.entities.Author;
import kattsyn.dev.StatService.entities.Event;
import kattsyn.dev.StatService.entities.Post;
import kattsyn.dev.StatService.enums.Events;
import kattsyn.dev.StatService.repositories.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void updatePost(Event event) {
        switch (event.getType()) {
            case EVENT_LIKE -> updateLike(event);
            case EVENT_VIEW -> updateView(event);
        }
    }

    private void updateLike(Event event) {
        Optional<Post> post = postRepository.findById(event.getPostId());
        if (post.isPresent()) {
            post.get().increaseLikes(1);
        } else {
            //todo: если поста нет, то создать сначала автора с одним лайком, затем пост и привязать автора к посту
            //Author author = new Author(event.ge)
        }
    }

    private void updateView(Event event) {

    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
