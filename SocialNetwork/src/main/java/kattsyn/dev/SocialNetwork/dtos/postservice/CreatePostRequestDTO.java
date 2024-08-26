package kattsyn.dev.SocialNetwork.dtos.postservice;


import lombok.Data;

@Data
public class CreatePostRequestDTO {
    private String header;
    private String postContent;
}
