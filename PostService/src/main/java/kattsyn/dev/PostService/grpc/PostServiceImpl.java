package kattsyn.dev.PostService.grpc;

import io.grpc.stub.StreamObserver;
import kattsyn.dev.PostService.mappers.GrpcToMainService;
import kattsyn.dev.grpc.*;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@AllArgsConstructor
@GrpcService
public class PostServiceImpl extends PostServiceGrpc.PostServiceImplBase {

    private final GrpcToMainService mapper;

    @Override
    public void getPostsByIdList(GetPostsByIdListRequest request, StreamObserver<GetPostsByIdListResponse> responseObserver) {
        mapper.getPostsByIdList(request, responseObserver);
    }

    @Override
    public void getPostById(GetPostByIdRequest request, StreamObserver<PostResponse> responseObserver) {
        mapper.getPostById(request, responseObserver);
    }

    @Override
    public void getAuthorId(GetAuthorByPostIdRequest request, StreamObserver<GetAuthorByPostIdResponse> responseObserver) {
        mapper.getAuthorId(request, responseObserver);
    }

    @Override
    public void getPosts(GetPostsRequest request, StreamObserver<PostPageResponse> responseObserver) {
        mapper.getPosts(request, responseObserver);
    }

    @Override
    public void editPost(EditPostRequest request, StreamObserver<EditPostResponse> responseObserver) {
        mapper.editPost(request, responseObserver);
    }

    @Override
    public void deletePost(DeletePostRequest request, StreamObserver<DeletePostResponse> responseObserver) {
        mapper.deletePost(request, responseObserver);
    }

    @Override
    public void createPost(CreatePostRequest request, StreamObserver<CreatePostResponse> responseObserver) {
        mapper.createPost(request, responseObserver);
    }
}


