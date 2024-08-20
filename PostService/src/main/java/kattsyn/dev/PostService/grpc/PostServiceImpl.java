package kattsyn.dev.PostService.grpc;

import io.grpc.stub.StreamObserver;
import kattsyn.dev.PostService.mappers.GrpcToMainService;
import kattsyn.dev.SocialNetwork.grpc.PostServiceGrpc;
import kattsyn.dev.SocialNetwork.grpc.PostServiceOuterClass;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@AllArgsConstructor
@GrpcService
public class PostServiceImpl extends PostServiceGrpc.PostServiceImplBase {

    private final GrpcToMainService mapper;

    @Override
    public void getPostById(PostServiceOuterClass.GetPostByIdRequest request, StreamObserver<PostServiceOuterClass.PostResponse> responseObserver) {
        mapper.getPostById(request, responseObserver);
    }

    @Override
    public void getPosts(PostServiceOuterClass.GetPostsRequest request, StreamObserver<PostServiceOuterClass.PostPageResponse> responseObserver) {
        mapper.getPosts(request, responseObserver);
    }

    @Override
    public void editPost(PostServiceOuterClass.EditPostRequest request, StreamObserver<PostServiceOuterClass.EditPostResponse> responseObserver) {
        mapper.editPost(request, responseObserver);
    }

    @Override
    public void deletePost(PostServiceOuterClass.DeletePostRequest request, StreamObserver<PostServiceOuterClass.DeletePostResponse> responseObserver) {
        mapper.deletePost(request, responseObserver);
    }

    @Override
    public void createPost(PostServiceOuterClass.CreatePostRequest request, StreamObserver<PostServiceOuterClass.CreatePostResponse> responseObserver) {
        mapper.createPost(request, responseObserver);
    }
}
