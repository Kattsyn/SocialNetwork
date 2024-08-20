package kattsyn.dev.PostService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPostsRequest {
    private int page;
    private int count;
}
