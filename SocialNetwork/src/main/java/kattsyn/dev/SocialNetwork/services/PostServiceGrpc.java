package kattsyn.dev.SocialNetwork.services;

import kattsyn.dev.SocialNetwork.grpc.PostServiceOuterClass;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostServiceGrpc {

    kattsyn.dev.SocialNetwork.grpc.PostServiceGrpc.PostServiceBlockingStub postServiceStub;

    public String getPostById(Long id) {
        PostServiceOuterClass.GetPostByIdRequest request = PostServiceOuterClass.GetPostByIdRequest.newBuilder()
                .setPostId(id)
                .build();
        PostServiceOuterClass.PostResponse response = postServiceStub.getPostById(request).next();

        return response.toString();
    }

    public String createPost(Long authorId, String header, String text) {
        PostServiceOuterClass.CreatePostRequest request = PostServiceOuterClass.CreatePostRequest.newBuilder()
                .setAuthorId(authorId)
                .setHeader(header)
                .setText(text)
                .build();
        PostServiceOuterClass.CreatePostResponse response = postServiceStub.createPost(request).next();
        return response.toString();
    }
}
