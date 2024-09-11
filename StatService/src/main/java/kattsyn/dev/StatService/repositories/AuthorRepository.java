package kattsyn.dev.StatService.repositories;

import kattsyn.dev.StatService.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
