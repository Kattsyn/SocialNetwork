package kattsyn.dev.SocialNetwork.services;

import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import kattsyn.dev.SocialNetwork.dtos.postservice.PostDTO;
import kattsyn.dev.SocialNetwork.entities.User;
import kattsyn.dev.SocialNetwork.exceptions.AppException;
import kattsyn.dev.grpc.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StatServiceGrpc {

    private final kattsyn.dev.grpc.StatServiceGrpc.StatServiceBlockingStub statServiceStub;
    private final UserService userService;
    private final PostServiceGrpc postServiceGrpc;

    public List<PostDTO> getNPostsByLikes(int N) throws AppException {
        PostsAuthorsByLikesViewsRequest request = PostsAuthorsByLikesViewsRequest.newBuilder()
                .setN(N)
                .build();
        try {
            PostsResponse postsResponse = statServiceStub.getNPostsByLikes(request).next();
            List<Long> idList = postsResponse.getPostIdList();
            return postServiceGrpc.getPostsByIdList(idList);
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.BAD_REQUEST, status.getMessage());
        }
    }

    public List<PostDTO> getNPostsByViews(int N) throws AppException {
        PostsAuthorsByLikesViewsRequest request = PostsAuthorsByLikesViewsRequest.newBuilder()
                .setN(N)
                .build();
        try {
            PostsResponse postsResponse = statServiceStub.getNPostsByViews(request).next();
            List<Long> idList = postsResponse.getPostIdList();
            return postServiceGrpc.getPostsByIdList(idList);
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.BAD_REQUEST, status.getMessage());
        }
    }

    public List<User> getNAuthorsByLikes(int N) throws AppException {
        PostsAuthorsByLikesViewsRequest request = PostsAuthorsByLikesViewsRequest.newBuilder()
                .setN(N)
                .build();
        try {
            AuthorsResponse postsResponse = statServiceStub.getNAuthorsByLikes(request).next();
            List<Long> idList = postsResponse.getAuthorIdList();
            return userService.getUsersByIdList(idList);
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.BAD_REQUEST, status.getMessage());
        }
    }

    public List<User> getNAuthorsByViews(int N) throws AppException {
        PostsAuthorsByLikesViewsRequest request = PostsAuthorsByLikesViewsRequest.newBuilder()
                .setN(N)
                .build();
        try {
            AuthorsResponse postsResponse = statServiceStub.getNAuthorsByViews(request).next();
            List<Long> idList = postsResponse.getAuthorIdList();
            return userService.getUsersByIdList(idList);
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.BAD_REQUEST, status.getMessage());
        }
    }


    public int getLikesById(Long postId) throws AppException {
        LikesViewsByIdRequest request = LikesViewsByIdRequest.newBuilder().setPostId(postId).build();
        try {
            LikesByIdResponse likesResponse = statServiceStub.getLikesById(request).next();
            return likesResponse.getLikes();
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.BAD_REQUEST, status.getMessage());
        }
    }

    public int getViewsById(Long postId) throws AppException {
        LikesViewsByIdRequest request = LikesViewsByIdRequest.newBuilder().setPostId(postId).build();
        try {
            ViewsByIdResponse viewsResponse = statServiceStub.getViewsById(request).next();
            return viewsResponse.getViews();
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.BAD_REQUEST, status.getMessage());
        }
    }
}
