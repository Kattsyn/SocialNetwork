package kattsyn.dev.PostService.repositories;

import kattsyn.dev.PostService.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByPostId(Long id);
}
