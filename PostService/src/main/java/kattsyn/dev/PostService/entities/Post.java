package kattsyn.dev.PostService.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "posts")
public class Post {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long postId;
    @Column(name = "author_id")
    Long authorId;
    @Column(name = "header")
    String header;
    @Column(name = "text")
    String text;

    public Post(Long authorId, String header, String text) {
        this.authorId = authorId;
        this.header = header;
        this.text = text;
    }
}
