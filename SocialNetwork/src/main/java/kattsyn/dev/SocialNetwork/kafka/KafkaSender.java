package kattsyn.dev.SocialNetwork.kafka;

import kattsyn.dev.models.kafka.Event;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaSender {

    private final KafkaTemplate<String, Event> kafkaTemplate;

    public void sendEvent(Event event, String topicName) {
        log.info("---------------------");
        log.info("Sending Json Serializer : {}", event);
        log.info("---------------------");

        kafkaTemplate.send(topicName, event);
    }
}
