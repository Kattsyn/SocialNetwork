package kattsyn.dev.SocialNetwork.dtos.postservice;


import lombok.Data;

@Data
public class CreatePostRequestDTO {
    private Long authorId;
    private String header;
    private String text;
}
