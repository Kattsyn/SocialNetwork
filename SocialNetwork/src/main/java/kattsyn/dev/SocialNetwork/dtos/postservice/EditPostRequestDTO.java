package kattsyn.dev.SocialNetwork.dtos.postservice;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditPostRequestDTO {
    private String header;
    private String postContent;
}
