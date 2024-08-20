package kattsyn.dev.PostService.mappers;

import io.grpc.stub.StreamObserver;
import kattsyn.dev.PostService.dtos.CreatePostRequest;
import kattsyn.dev.PostService.dtos.DeletePostRequest;
import kattsyn.dev.PostService.dtos.EditPostRequest;
import kattsyn.dev.PostService.dtos.GetPostsRequest;
import kattsyn.dev.PostService.entities.Post;
import kattsyn.dev.PostService.services.PostServiceMain;
import kattsyn.dev.SocialNetwork.grpc.PostServiceOuterClass;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class GrpcToMainService {

    private final PostServiceMain postServiceMain;

    public void getPostById(PostServiceOuterClass.GetPostByIdRequest request, StreamObserver<PostServiceOuterClass.PostResponse> responseObserver) {

        Optional<Post> post = postServiceMain.getPostById(request.getPostId());

        if (post.isPresent()) {
            PostServiceOuterClass.PostResponse response = PostServiceOuterClass.PostResponse.newBuilder()
                    .setPostId(post.get().getPostId())
                    .setAuthorId(post.get().getAuthorId())
                    .setHeader(post.get().getHeader())
                    .setText(post.get().getText())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    public void getPosts(PostServiceOuterClass.GetPostsRequest request, StreamObserver<PostServiceOuterClass.PostPageResponse> responseObserver) {
        List<Post> list = postServiceMain.getPosts(new GetPostsRequest(request.getPage(), request.getCount()));

        List<PostServiceOuterClass.PostResponse> listResponses = list.stream().map(e -> PostServiceOuterClass.PostResponse
                        .newBuilder()
                        .setPostId(e.getPostId())
                        .setAuthorId(e.getAuthorId())
                        .setHeader(e.getHeader())
                        .setText(e.getText()).build())
                .toList();

        PostServiceOuterClass.PostPageResponse response = PostServiceOuterClass.PostPageResponse.newBuilder()
                .addAllPosts(listResponses)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void editPost(PostServiceOuterClass.EditPostRequest request, StreamObserver<PostServiceOuterClass.EditPostResponse> responseObserver) {

        Post post = postServiceMain.editPost(new EditPostRequest(
                request.getPostId(),
                request.getUserId(),
                request.getHeader(),
                request.getText()
        ));

        //todo: добавить что-то типа типа ответа, если всё хорошо, например через enum. На созвоне обсуждали
        PostServiceOuterClass.EditPostResponse response = PostServiceOuterClass.EditPostResponse.newBuilder()
                .setResponse("Edited Successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void deletePost(PostServiceOuterClass.DeletePostRequest request, StreamObserver<PostServiceOuterClass.DeletePostResponse> responseObserver) {

        postServiceMain.deletePost(new DeletePostRequest(request.getPostId(), 1L /*todo: добавить в .proto второй аргумент */ ));

        //todo: добавить что-то типа типа ответа, если всё хорошо, например через enum. На созвоне обсуждали
        PostServiceOuterClass.DeletePostResponse response = PostServiceOuterClass.DeletePostResponse.newBuilder()
                .setResponse("Post " + request.getPostId() + " deleted successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();


    }

    public void createPost(PostServiceOuterClass.CreatePostRequest request, StreamObserver<PostServiceOuterClass.CreatePostResponse> responseObserver) {
        Post post = postServiceMain.createPost(new CreatePostRequest(request.getAuthorId(), request.getHeader(), request.getText()));

        //todo: добавить что-то типа типа ответа, если всё хорошо, например через enum. На созвоне обсуждали
        PostServiceOuterClass.CreatePostResponse response = PostServiceOuterClass.CreatePostResponse.newBuilder()
                .setResponse("Successfully added new post: " + post)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
