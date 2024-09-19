package kattsyn.dev.StatService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "post_id")
    Long postId;
    @Column(name = "user_id")
    Long userId;

    public Like(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
