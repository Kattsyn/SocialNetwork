package kattsyn.dev.PostService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePostRequestDTO {
    Long authorId;
    String header;
    String postContent;
}
