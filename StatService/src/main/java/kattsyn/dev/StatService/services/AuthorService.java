package kattsyn.dev.StatService.services;

import kattsyn.dev.StatService.entities.Author;
import kattsyn.dev.StatService.entities.Post;
import kattsyn.dev.StatService.repositories.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public Author save(Long id) {
        return authorRepository.save(new Author(id));
    }

    /*
    public Author save(Long authorId, Long postId) {
        Optional<Post> post = postService.findById(postId);
        return post.map(value -> authorRepository.save(new Author(authorId, value))).orElseGet(() -> save(authorId));
    }
    //todo: Возможно лишнее
    т.к. в PostService если не обнаруживается поста, то сначала создается автор, потом к нему пост привязывается
     */

    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }
}
