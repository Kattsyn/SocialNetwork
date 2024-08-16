package kattsyn.dev.PostService.grpc;

import io.grpc.stub.StreamObserver;
import kattsyn.dev.PostService.entities.Post;
import kattsyn.dev.PostService.repositories.PostRepository;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@GrpcService
public class PostServiceImpl extends PostServiceGrpc.PostServiceImplBase {
    PostRepository postRepository;

    @Override
    public void getPostById(PostServiceOuterClass.GetPostByIdRequest request, StreamObserver<PostServiceOuterClass.PostResponse> responseObserver) {
        Optional<Post> post = postRepository.findByPostId(request.getPostId());
        PostServiceOuterClass.PostResponse response;
        if (post.isPresent()) {
            response = PostServiceOuterClass.PostResponse.newBuilder()
                    .setPostId(post.get().getPostId())
                    .setAuthorId(post.get().getAuthorId())
                    .setHeader(post.get().getHeader())
                    .setText(post.get().getText())
                    .build();
            System.out.println(response);
            responseObserver.onNext(response);
        } else {
            //todo: выбросить исключение
            response = PostServiceOuterClass.PostResponse.newBuilder()
                    .setPostId(-1)
                    .setAuthorId(-1)
                    .setHeader("Post not found")
                    .setText("Post not found")
                    .build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getPosts(PostServiceOuterClass.GetPostsRequest request, StreamObserver<PostServiceOuterClass.PostPageResponse> responseObserver) {
        Page<Post> page = postRepository.findAll(PageRequest.of(request.getPage(), request.getCount()));

        List<PostServiceOuterClass.PostResponse> listResponses = page.getContent().stream().map(e -> PostServiceOuterClass.PostResponse
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
        System.out.println(response);

    }

    @Override
    public void editPost(PostServiceOuterClass.EditPostRequest request, StreamObserver<PostServiceOuterClass.EditPostResponse> responseObserver) {

        Post postRequest = new Post(
                request.getPostId(),
                request.getUserId(),
                request.getHeader(),
                request.getText());

        Optional<Post> postById = postRepository.findByPostId(postRequest.getPostId());
        PostServiceOuterClass.EditPostResponse response;

        if (postById.isPresent()) {
            if (postById.get().getAuthorId().equals(postRequest.getAuthorId())) {
                if (!postRequest.getHeader().isEmpty()) {
                    postById.get().setHeader(postRequest.getHeader());
                }
                if (!postRequest.getText().isEmpty()) {
                    postById.get().setText(postRequest.getText());
                }
                postRepository.save(postById.get());
                response = PostServiceOuterClass.EditPostResponse.newBuilder()
                        .setResponse("Post edited successfully")
                        .build();
            } else {
                response = PostServiceOuterClass.EditPostResponse.newBuilder()
                        .setResponse("You are not author of post id: " + postRequest.getPostId())
                        .build();
            }
        } else {
            response = PostServiceOuterClass.EditPostResponse.newBuilder()
                    .setResponse("Cant find post id: " + postRequest.getPostId())
                    .build();
        }
        System.out.println(response);

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void deletePost(PostServiceOuterClass.DeletePostRequest request, StreamObserver<PostServiceOuterClass.DeletePostResponse> responseObserver) {

        postRepository.deleteById(request.getPostId());
        PostServiceOuterClass.DeletePostResponse response = PostServiceOuterClass.DeletePostResponse.newBuilder()
                .setResponse("Post " + request.getPostId() + " deleted successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();


    }

    @Override
    public void createPost(PostServiceOuterClass.CreatePostRequest request, StreamObserver<PostServiceOuterClass.CreatePostResponse> responseObserver) {
        Post post = new Post(
                request.getAuthorId(),
                request.getHeader(),
                request.getText());

        postRepository.save(post);

        //todo: Добавить выбрасывание исключений
        PostServiceOuterClass.CreatePostResponse response = PostServiceOuterClass.CreatePostResponse.newBuilder()
                .setResponse("Successfully added new post: " + post)
                .build();
        System.out.println(response);

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
