package kattsyn.dev.StatService.services;

import kattsyn.dev.StatService.dtos.EventCreationDto;
import kattsyn.dev.StatService.entities.Event;
import kattsyn.dev.StatService.repositories.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public void save(EventCreationDto eventCreationDto) {
        Event event = eventRepository.save(new Event(
                eventCreationDto.getType(),
                eventCreationDto.getUserId(),
                eventCreationDto.getPostId()));

    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }
}
