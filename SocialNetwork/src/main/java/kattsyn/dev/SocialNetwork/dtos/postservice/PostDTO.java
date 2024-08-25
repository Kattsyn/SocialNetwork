package kattsyn.dev.SocialNetwork.dtos.postservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostDTO {
    private Long postId;
    private Long authorId;
    private String header;
    private String postContent;
}
