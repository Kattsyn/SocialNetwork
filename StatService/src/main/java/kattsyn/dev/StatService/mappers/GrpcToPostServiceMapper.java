package kattsyn.dev.StatService.mappers;

import io.grpc.stub.StreamObserver;
import kattsyn.dev.StatService.entities.Author;
import kattsyn.dev.StatService.entities.Post;
import kattsyn.dev.StatService.services.AuthorService;
import kattsyn.dev.StatService.services.PostService;
import kattsyn.dev.grpc.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GrpcToPostServiceMapper {

    private final PostService postService;
    private final AuthorService authorService;

    public void getNPostsByLikes(PostsAuthorsByLikesViewsRequest request, StreamObserver<PostsResponse> responseObserver) {
        List<Post> postsIdList = postService.findNBestPostsByLikes(request.getN());
        List<Long> listResponses = postsIdList.stream().map(Post::getId).toList();

        responseObserver.onNext(PostsResponse.newBuilder().addAllPostId(listResponses).build());
        responseObserver.onCompleted();
    }


    public void getNPostsByViews(PostsAuthorsByLikesViewsRequest request, StreamObserver<PostsResponse> responseObserver) {
        List<Post> postsIdList = postService.findNBestPostsByViews(request.getN());
        List<Long> listResponses = postsIdList.stream().map(Post::getId).toList();

        responseObserver.onNext(PostsResponse.newBuilder().addAllPostId(listResponses).build());
        responseObserver.onCompleted();
    }


    public void getNAuthorsByLikes(PostsAuthorsByLikesViewsRequest request, StreamObserver<AuthorsResponse> responseObserver) {
        List<Author> authorsIdList = authorService.findNBestAuthorsByLikes(request.getN());
        List<Long> listResponses = authorsIdList.stream().map(Author::getId).toList();

        responseObserver.onNext(AuthorsResponse.newBuilder().addAllAuthorId(listResponses).build());
        responseObserver.onCompleted();
    }


    public void getNAuthorsByViews(PostsAuthorsByLikesViewsRequest request, StreamObserver<AuthorsResponse> responseObserver) {
        List<Author> authorsIdList = authorService.findNBestAuthorsByViews(request.getN());
        List<Long> listResponses = authorsIdList.stream().map(Author::getId).toList();

        responseObserver.onNext(AuthorsResponse.newBuilder().addAllAuthorId(listResponses).build());
        responseObserver.onCompleted();
    }


    public void getLikesById(LikesViewsByIdRequest request, StreamObserver<LikesByIdResponse> responseObserver) {
        LikesByIdResponse response = LikesByIdResponse
                .newBuilder()
                .setLikes(postService.getLikesByPostId(request.getPostId()))
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    public void getViewsById(LikesViewsByIdRequest request, StreamObserver<ViewsByIdResponse> responseObserver) {
        ViewsByIdResponse response = ViewsByIdResponse
                .newBuilder()
                .setViews(postService.getViewsByPostId(request.getPostId()))
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
