package kattsyn.dev.PostService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditPostRequest {
    private Long postId;
    private Long userId;
    private String header;
    private String text;
}
