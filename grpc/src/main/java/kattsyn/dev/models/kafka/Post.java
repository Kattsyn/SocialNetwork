package kattsyn.dev.models.kafka;

public class Post {
    Long postId;
    Long authorId;
    String header;
    String postContent;


    public Post(Long postId, Long authorId, String header, String postContent) {
        this.postId = postId;
        this.authorId = authorId;
        this.header = header;
        this.postContent = postContent;
    }
}
