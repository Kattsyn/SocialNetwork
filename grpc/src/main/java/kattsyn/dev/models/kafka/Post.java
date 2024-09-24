package kattsyn.dev.models.kafka;

public class Post {
    private Long postId;
    private Long authorId;
    private String header;
    private String postContent;


    public Post(Long postId, Long authorId, String header, String postContent) {
        this.postId = postId;
        this.authorId = authorId;
        this.header = header;
        this.postContent = postContent;
    }

    public Post(){}

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", authorId=" + authorId +
                ", header='" + header + '\'' +
                ", postContent='" + postContent + '\'' +
                '}';
    }
}
