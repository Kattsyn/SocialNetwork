package kattsyn.dev.StatService.entities;

import jakarta.persistence.*;
import kattsyn.dev.StatService.enums.Events;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Events type;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "post_id")
    private Long postId;
    private LocalDateTime timestamp;

    public Event(Events type, Long userId, Long postId) {
        this.type = type;
        this.userId = userId;
        this.postId = postId;
        this.timestamp = LocalDateTime.now();
    }
}
