package kattsyn.dev.StatService.repositories;

import kattsyn.dev.StatService.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
