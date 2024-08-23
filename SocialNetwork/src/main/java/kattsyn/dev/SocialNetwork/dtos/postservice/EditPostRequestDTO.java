package kattsyn.dev.SocialNetwork.dtos.postservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditPostRequestDTO {
    private Long postId;
    private Long userId;
    private String header;
    private String text;
}
