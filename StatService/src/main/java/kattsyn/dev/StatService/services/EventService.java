package kattsyn.dev.StatService.services;

import kattsyn.dev.StatService.entities.Event;
import kattsyn.dev.StatService.enums.Events;
import kattsyn.dev.StatService.repositories.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public void save(Event event) {
        eventRepository.save(event);
    }

    public Optional<Event> findByPostIdAndUserIdAndType(Long postId, Long userId, Events type) {
        return eventRepository.findByPostIdAndUserIdAndType(postId, userId, type);
    }
}
