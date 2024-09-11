package kattsyn.dev.StatService.grpc;

import io.grpc.stub.StreamObserver;
import kattsyn.dev.StatService.mappers.GrpcToPostServiceMapper;
import kattsyn.dev.grpc.*;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@AllArgsConstructor
public class StatServiceImpl extends StatServiceGrpc.StatServiceImplBase {

    private final GrpcToPostServiceMapper mapper;

    @Override
    public void getNPostsByLikes(PostsAuthorsByLikesViewsRequest request, StreamObserver<PostsResponse> responseObserver) {
        mapper.getNPostsByLikes(request, responseObserver);
    }

    @Override
    public void getNPostsByViews(PostsAuthorsByLikesViewsRequest request, StreamObserver<PostsResponse> responseObserver) {
        mapper.getNPostsByViews(request, responseObserver);
    }

    @Override
    public void getNAuthorsByLikes(PostsAuthorsByLikesViewsRequest request, StreamObserver<AuthorsResponse> responseObserver) {
        mapper.getNAuthorsByLikes(request, responseObserver);
    }

    @Override
    public void getNAuthorsByViews(PostsAuthorsByLikesViewsRequest request, StreamObserver<AuthorsResponse> responseObserver) {
        mapper.getNAuthorsByViews(request, responseObserver);
    }

    @Override
    public void getLikesById(LikesViewsByIdRequest request, StreamObserver<LikesByIdResponse> responseObserver) {
        mapper.getLikesById(request, responseObserver);
    }

    @Override
    public void getViewsById(LikesViewsByIdRequest request, StreamObserver<ViewsByIdResponse> responseObserver) {
        mapper.getViewsById(request, responseObserver);
    }
}
