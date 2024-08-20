package kattsyn.dev.PostService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletePostRequest {
    private Long postId;
    private Long authorId;
}
