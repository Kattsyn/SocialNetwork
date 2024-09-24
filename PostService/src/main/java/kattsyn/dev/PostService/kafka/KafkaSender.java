package kattsyn.dev.PostService.kafka;

import kattsyn.dev.models.kafka.Post;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaSender {

    private final KafkaTemplate<String, Post> kafkaTemplate;

    public void sendPost(Post post, String topicName) {
        log.info("---------------------");
        log.info("Sending Json Serializer : {}", post);
        log.info("---------------------");

        kafkaTemplate.send(topicName, post);
    }
}
