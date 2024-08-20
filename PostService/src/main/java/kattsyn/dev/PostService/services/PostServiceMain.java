package kattsyn.dev.PostService.services;

import kattsyn.dev.PostService.dtos.CreatePostRequest;
import kattsyn.dev.PostService.dtos.DeletePostRequest;
import kattsyn.dev.PostService.dtos.EditPostRequest;
import kattsyn.dev.PostService.dtos.GetPostsRequest;
import kattsyn.dev.PostService.entities.Post;
import kattsyn.dev.PostService.repositories.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostServiceMain {
    private final PostRepository postRepository;

    public Optional<Post> getPostById(Long id) {
        Optional<Post> post = postRepository.findByPostId(id);
        //PostServiceOuterClass.PostResponse response;
        if (post.isPresent()) {
            return post;
        } else {
            //todo: поменять на кастомное исключение и добавить глобальный обработчик исключений
            throw new RuntimeException("Пользователь не найден");
        }
    }

    public List<Post> getPosts(GetPostsRequest request) {
        Page<Post> page = postRepository.findAll(PageRequest.of(request.getPage(), request.getCount()));

        return page.getContent();
    }


    public Post editPost(EditPostRequest request) {

        Post postRequest = new Post(
                request.getPostId(),
                request.getUserId(),
                request.getHeader(),
                request.getText());

        Optional<Post> postById = postRepository.findByPostId(postRequest.getPostId());

        if (postById.isPresent()) {
            if (postById.get().getAuthorId().equals(postRequest.getAuthorId())) {
                if (!postRequest.getHeader().isEmpty()) {
                    postById.get().setHeader(postRequest.getHeader());
                }
                if (!postRequest.getText().isEmpty()) {
                    postById.get().setText(postRequest.getText());
                }
                return postRepository.save(postById.get());
            } else {
                //todo: пробросить кастомное исключение с кодом 404
                throw new RuntimeException("POST NOT FOUND");
            }
        } else {
            //todo: пробросить кастомное исключение с кодом 404
            throw new RuntimeException("POST NOT FOUND");
        }
    }

    public void deletePost(DeletePostRequest request) {
        Optional<Post> post = postRepository.findByPostId(request.getPostId());

        if (post.isPresent()) {
            if (post.get().getAuthorId().equals(request.getAuthorId())) {
                postRepository.deleteById(request.getPostId());
            } else {
                //todo: пробросить кастомное исключение с кодом 403
                throw new RuntimeException("FORBIDDEN");
            }
        } else {
            //todo: пробросить кастомное исключение с кодом 404
            throw new RuntimeException("POST NOT FOUND");
        }
    }

    public Post createPost(CreatePostRequest request) {
        Post post = new Post(
                request.getAuthorId(),
                request.getHeader(),
                request.getText());

        return postRepository.save(post);
    }
}
