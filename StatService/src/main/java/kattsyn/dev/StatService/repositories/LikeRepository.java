package kattsyn.dev.StatService.repositories;

import kattsyn.dev.StatService.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
