package kattsyn.dev.PostService.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(name = "post_content")
    String postContent;

    public Post(Long authorId, String header, String text) {
        this.authorId = authorId;
        this.header = header;
        this.postContent = text;
    }
}
