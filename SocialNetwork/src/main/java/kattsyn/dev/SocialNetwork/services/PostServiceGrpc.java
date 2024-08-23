package kattsyn.dev.SocialNetwork.services;

import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import kattsyn.dev.SocialNetwork.dtos.postservice.DeletePostRequestDTO;
import kattsyn.dev.SocialNetwork.dtos.postservice.EditPostRequestDTO;
import kattsyn.dev.SocialNetwork.dtos.postservice.PostDTO;
import kattsyn.dev.SocialNetwork.exceptions.AppException;
import kattsyn.dev.grpc.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceGrpc {

    kattsyn.dev.grpc.PostServiceGrpc.PostServiceBlockingStub postServiceStub;

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
                    .map(e -> new PostDTO(e.getPostId(), e.getAuthorId(), e.getHeader(), e.getText()))
                    .toList();
            return list;
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.NOT_FOUND, status.getMessage());
        }
    }

    public String createPost(Long authorId, String header, String text) throws AppException {
        CreatePostRequest request = CreatePostRequest.newBuilder()
                .setAuthorId(authorId)
                .setHeader(header)
                .setText(text)
                .build();
        try {
            CreatePostResponse response = postServiceStub.createPost(request).next();
            return response.toString();
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.NOT_FOUND, status.getMessage());
        }
    }

    public String editPost(EditPostRequestDTO requestDTO) throws AppException {
        //todo: выдает ошибку, если передать меньше параметров
        EditPostRequest request = EditPostRequest.newBuilder()
                .setPostId(requestDTO.getPostId())
                .setUserId(requestDTO.getUserId())
                .setHeader(requestDTO.getHeader())
                .setText(requestDTO.getText())
                .build();

        try {
            EditPostResponse response = postServiceStub.editPost(request).next();
            return response.toString();
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.NOT_FOUND, status.getMessage());
        }
    }

    public String deletePost(DeletePostRequestDTO requestDTO) throws AppException {
        DeletePostRequest request = DeletePostRequest.newBuilder()
                .setPostId(requestDTO.getPostId())
                .setAuthorId(requestDTO.getUserId())
                .build();
        try {
            DeletePostResponse response = postServiceStub.deletePost(request).next();
            return response.toString();
        } catch (Exception e) {
            Status status = StatusProto.fromThrowable(e);
            throw new AppException(HttpStatus.NOT_FOUND, status.getMessage());
        }
    }
}
