package kattsyn.dev.StatService.repositories;

import kattsyn.dev.StatService.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    boolean existsById(Long id);
}
