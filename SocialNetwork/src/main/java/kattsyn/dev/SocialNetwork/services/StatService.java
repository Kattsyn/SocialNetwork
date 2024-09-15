package kattsyn.dev.SocialNetwork.services;

import kattsyn.dev.models.kafka.Event;
import kattsyn.dev.models.kafka.Events;
import kattsyn.dev.SocialNetwork.utils.KafkaSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatService {

    private final KafkaSender kafkaSender;

    public void sendMessage() {
        kafkaSender.sendEvent(new Event(Events.EVENT_LIKE, 1L, 1L), "event");
    }

    public void likePost(Long postId, Long userId) {
        Event event = new Event(Events.EVENT_LIKE, userId, postId);
        kafkaSender.sendEvent(event, "event");
    }

    public void viewPost(Long postId, Long userId) {
        Event event = new Event(Events.EVENT_VIEW, userId, postId);
        kafkaSender.sendEvent(event, "event");
    }
}
