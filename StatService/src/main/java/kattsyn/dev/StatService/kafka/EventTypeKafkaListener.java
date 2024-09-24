package kattsyn.dev.StatService.kafka;

import kattsyn.dev.models.kafka.Event;
import kattsyn.dev.StatService.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(id = "eventListener", topics = "event", containerFactory = "eventKafkaListenerContainerFactory")
public class EventTypeKafkaListener {

    private final PostService postService;

    @KafkaHandler
    public void listenGroupEvent(String data) {
        log.info("Received message [{}] in groupMessage", data);
    }


    @KafkaHandler
    void listenGroupEvent(Event event) {
        log.info("Received event [{}] in groupEvent", event);
        System.out.println(event);
        postService.processEvent(kattsyn.dev.StatService.entities.Event.modelToEntity(event));
    }
}
