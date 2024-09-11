package kattsyn.dev.models.kafka;

import java.time.LocalDateTime;
import java.time.ZoneId;


public class Event {
    private Events type;
    private Long userId;
    private Long postId;
    private LocalDateTime timestamp;

    public Event(Events type, Long userId, Long postId) {
        this.type = type;
        this.userId = userId;
        this.postId = postId;
        this.timestamp = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
    }

    public Event() {
    }

    public Events getType() {
        return type;
    }

    public void setType(Events type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
