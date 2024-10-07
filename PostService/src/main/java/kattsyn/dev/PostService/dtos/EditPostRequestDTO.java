package kattsyn.dev.PostService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditPostRequestDTO {
    private Long postId;
    private Long userId;
    private String header;
    private String postContent;
}
