package kattsyn.dev.PostService.mappers;


import io.grpc.stub.StreamObserver;
import kattsyn.dev.PostService.entities.Post;
import kattsyn.dev.PostService.services.PostServiceMain;
import kattsyn.dev.grpc.*;
import kattsyn.dev.grpc.GetPostsRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import kattsyn.dev.PostService.dtos.*;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class GrpcToMainService {

    private final PostServiceMain postServiceMain;

    public void getPostById(GetPostByIdRequest request, StreamObserver<PostResponse> responseObserver) {

        Optional<Post> post = postServiceMain.getPostById(request.getPostId());

        if (post.isPresent()) {
            PostResponse response = PostResponse.newBuilder()
                    .setPostId(post.get().getPostId())
                    .setAuthorId(post.get().getAuthorId())
                    .setHeader(post.get().getHeader())
                    .setPostContent(post.get().getPostContent())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    public void getPosts(GetPostsRequest request, StreamObserver<PostPageResponse> responseObserver) {
        List<Post> list = postServiceMain.getPosts(new GetPostsRequestDTO(request.getPage(), request.getCount()));

        List<PostResponse> listResponses = list.stream().map(e -> PostResponse
                        .newBuilder()
                        .setPostId(e.getPostId())
                        .setAuthorId(e.getAuthorId())
                        .setHeader(e.getHeader())
                        .setPostContent(e.getPostContent()).build())
                .toList();

        PostPageResponse response = PostPageResponse.newBuilder()
                .addAllPosts(listResponses)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void editPost(EditPostRequest request, StreamObserver<EditPostResponse> responseObserver) {

        postServiceMain.editPost(new EditPostRequestDTO(
                request.getPostId(),
                request.getUserId(),
                request.getHeader(),
                request.getPostContent()
        ));
        EditPostResponse response = EditPostResponse.newBuilder()
                .setResponse(SuccessResponse.OK)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void deletePost(DeletePostRequest request, StreamObserver<DeletePostResponse> responseObserver) {

        postServiceMain.deletePost(new DeletePostRequestDTO(request.getPostId(), request.getAuthorId() ));

        DeletePostResponse response = DeletePostResponse.newBuilder()
                .setResponse(SuccessResponse.OK)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    public void createPost(CreatePostRequest request, StreamObserver<CreatePostResponse> responseObserver) {
        postServiceMain.createPost(new CreatePostRequestDTO(request.getAuthorId(), request.getHeader(), request.getPostContent()));

        CreatePostResponse response = CreatePostResponse.newBuilder()
                .setResponse(SuccessResponse.CREATED)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}

