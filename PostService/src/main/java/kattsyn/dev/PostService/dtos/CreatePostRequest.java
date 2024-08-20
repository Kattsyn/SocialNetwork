package kattsyn.dev.PostService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePostRequest {
    Long authorId;
    String header;
    String text;
}
