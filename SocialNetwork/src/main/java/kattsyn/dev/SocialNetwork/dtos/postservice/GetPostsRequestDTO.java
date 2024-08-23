package kattsyn.dev.SocialNetwork.dtos.postservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPostsRequestDTO {
    private int page;
    private int count;
}
