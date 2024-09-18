package kattsyn.dev.StatService.kafka;


import kattsyn.dev.models.kafka.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(id = "postListener", topics = "post", containerFactory = "postKafkaListenerContainerFactory")
public class PostTypeKafkaListener {


    @KafkaHandler
    public void listenGroupEvent(String data) {
        log.info("Received message [{}] in groupMessage", data);
    }


    @KafkaHandler
    void listenGroupEvent(Post post) {
        log.info("Received event [{}] in groupPost", post);
        //todo: добавить добавление поста в таблицу
    }
}
