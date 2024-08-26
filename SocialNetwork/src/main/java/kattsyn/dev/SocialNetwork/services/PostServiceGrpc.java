package kattsyn.dev.SocialNetwork.services;

import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import kattsyn.dev.SocialNetwork.dtos.postservice.CreatePostRequestDTO;
import kattsyn.dev.SocialNetwork.dtos.postservice.DeletePostRequestDTO;
import kattsyn.dev.SocialNetwork.dtos.postservice.EditPostRequestDTO;
import kattsyn.dev.SocialNetwork.dtos.postservice.PostDTO;
import kattsyn.dev.SocialNetwork.exceptions.AppException;
import kattsyn.dev.grpc.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceGrpc {

    private final kattsyn.dev.grpc.PostServiceGrpc.PostServiceBlockingStub postServiceStub;
    private final UserService userService;

    public String getPostById(Long id) throws AppException {
        GetPostByIdRequest request = GetPostByIdRequest.newBuilder()
                .setPostId(id)
                .build();
        try {
            PostResponse response = postServiceStub.getPostById(request).next();
            return response.toString();
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.NOT_FOUND, status.getMessage());
        }
    }

    public List<PostDTO> getPosts(int page, int count) throws AppException {
        GetPostsRequest request = GetPostsRequest.newBuilder()
                .setPage(page)
                .setCount(count)
                .build();
        try {
            PostPageResponse response = postServiceStub.getPosts(request).next();
            List<PostDTO> list = response.getPostsList()
                    .stream()
                    .map(e -> new PostDTO(e.getPostId(), e.getAuthorId(), e.getHeader(), e.getPostContent()))
                    .toList();
            return list;
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.NOT_FOUND, status.getMessage());
        }
    }

    public String createPost(CreatePostRequestDTO requestDTO, Principal principal) throws AppException {
        CreatePostRequest request = CreatePostRequest.newBuilder()
                .setAuthorId(getUserIdByPrincipal(principal))
                .setHeader(requestDTO.getHeader())
                .setPostContent(requestDTO.getPostContent())
                .build();
        try {
            CreatePostResponse response = postServiceStub.createPost(request).next();
            return response.toString();
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.NOT_FOUND, status.getMessage());
        }
    }

    public String editPost(EditPostRequestDTO requestDTO, Principal principal) throws AppException {
        EditPostRequest.Builder builder = EditPostRequest.newBuilder()
                .setPostId(requestDTO.getPostId())
                .setUserId(getUserIdByPrincipal(principal));
        if (requestDTO.getHeader() != null) {
            builder.setHeader(requestDTO.getHeader());
        }
        if (requestDTO.getPostContent() != null) {
            builder.setPostContent(requestDTO.getPostContent());
        }

        try {
            EditPostResponse response = postServiceStub.editPost(builder.build()).next();
            return response.toString();
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.NOT_FOUND, status.getMessage());
        }
    }

    public String deletePost(Long postId, Principal principal) throws AppException {
        DeletePostRequest request = DeletePostRequest.newBuilder()
                .setPostId(postId)
                .setAuthorId(getUserIdByPrincipal(principal))
                .build();
        try {
            DeletePostResponse response = postServiceStub.deletePost(request).next();
            return response.toString();
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.NOT_FOUND, status.getMessage());
        }
    }

    private Long getUserIdByPrincipal(Principal principal) {
        return userService.findByUsername(principal.getName()).get().getId();
    }
}
