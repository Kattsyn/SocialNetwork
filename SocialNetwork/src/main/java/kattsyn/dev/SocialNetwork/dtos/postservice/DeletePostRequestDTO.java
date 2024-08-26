package kattsyn.dev.SocialNetwork.dtos.postservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletePostRequestDTO {
    private Long postId;
    private Long userId;
}
