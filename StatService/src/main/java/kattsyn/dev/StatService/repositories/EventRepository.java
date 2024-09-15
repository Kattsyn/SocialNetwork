package kattsyn.dev.StatService.repositories;

import kattsyn.dev.StatService.entities.Event;
import kattsyn.dev.StatService.enums.Events;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByPostIdAndUserIdAndType(Long postId, Long userId, Events type);
}
