package kattsyn.dev.PostService.services;

import kattsyn.dev.PostService.dtos.CreatePostRequestDTO;
import kattsyn.dev.PostService.dtos.DeletePostRequestDTO;
import kattsyn.dev.PostService.dtos.EditPostRequestDTO;
import kattsyn.dev.PostService.dtos.GetPostsRequestDTO;
import kattsyn.dev.PostService.entities.Post;
import kattsyn.dev.PostService.exceptions.PostServiceErrorCodes;
import kattsyn.dev.PostService.exceptions.PostServiceException;
import kattsyn.dev.PostService.repositories.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PostServiceMain {
    private final PostRepository postRepository;

    public List<Post> getPostListByIdList(List<Long> idList) {
        return idList.stream()
                .map(e -> {
                    Optional<Post> post = postRepository.findById(e);
                    if (post.isPresent()) {
                        return post.get();
                    } else {
                        log.info("Пост с id = {} не был найден.", e);
                        return null;
                    }
                }).toList();
    }

    public Post getPostById(Long id) {
        Optional<Post> post = postRepository.findByPostId(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            throw new PostServiceException(PostServiceErrorCodes.NOT_FOUND, "POST NOT FOUND");
        }
    }

    public Long getAuthorId(Long postId) {
        Post post = getPostById(postId);
        return post.getAuthorId();
    }

    public List<Post> getPosts(GetPostsRequestDTO request) {
        Page<Post> page = postRepository.findAll(PageRequest.of(request.getPage(), request.getCount()));

        return page.getContent();
    }


    public Post editPost(EditPostRequestDTO request) {

        Post postRequest = new Post(
                request.getPostId(),
                request.getUserId(),
                request.getHeader(),
                request.getPostContent());

        Optional<Post> postById = postRepository.findByPostId(postRequest.getPostId());

        if (postById.isPresent()) {
            if (postById.get().getAuthorId().equals(postRequest.getAuthorId())) {
                if (!postRequest.getHeader().isEmpty()) {
                    postById.get().setHeader(postRequest.getHeader());
                }
                if (!postRequest.getPostContent().isEmpty()) {
                    postById.get().setPostContent(postRequest.getPostContent());
                }
                return postRepository.save(postById.get());
            } else {
                throw new PostServiceException(PostServiceErrorCodes.NOT_FOUND, "POST NOT FOUND");
            }
        } else {
            throw new PostServiceException(PostServiceErrorCodes.NOT_FOUND, "POST NOT FOUND");
        }
    }

    public void deletePost(DeletePostRequestDTO request) {
        Optional<Post> post = postRepository.findByPostId(request.getPostId());

        if (post.isPresent()) {
            if (post.get().getAuthorId().equals(request.getAuthorId())) {
                postRepository.deleteById(request.getPostId());
            } else {
                throw new PostServiceException(PostServiceErrorCodes.NOT_FOUND, "POST NOT FOUND");
            }
        } else {
            throw new PostServiceException(PostServiceErrorCodes.NOT_FOUND, "POST NOT FOUND");
        }
    }

    public Post createPost(CreatePostRequestDTO request) {
        Post post = new Post(
                request.getAuthorId(),
                request.getHeader(),
                request.getPostContent());

        return postRepository.save(post);
    }
}
