package kattsyn.dev.SocialNetwork.dtos.postservice;


import lombok.Data;

@Data
public class PostDTO {
    private Long authorId;
    private String header;
    private String text;
}
