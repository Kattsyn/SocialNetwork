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
@Table(
        name = "likes",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "user_id"})}
)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "post_id")
    private Long postId;
    @Column(name = "user_id")
    private Long userId;

    public Like(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
