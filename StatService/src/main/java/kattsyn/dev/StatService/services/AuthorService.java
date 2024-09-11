package kattsyn.dev.StatService.services;

import kattsyn.dev.StatService.entities.Author;
import kattsyn.dev.StatService.repositories.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public List<Author> findNBestAuthorsByLikes(int N) {
        Page<Author> page = authorRepository.findAll(PageRequest.of(0, N, Sort.by("likes").descending()));
        return page.getContent();
    }

    public List<Author> findNBestAuthorsByViews(int N) {
        Page<Author> page = authorRepository.findAll(PageRequest.of(0, N, Sort.by("views").descending()));
        return page.getContent();
    }
}
