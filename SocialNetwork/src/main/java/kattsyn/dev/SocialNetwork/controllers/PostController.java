package kattsyn.dev.SocialNetwork.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kattsyn.dev.SocialNetwork.dtos.postservice.CreatePostRequestDTO;
import kattsyn.dev.SocialNetwork.dtos.postservice.DeletePostRequestDTO;
import kattsyn.dev.SocialNetwork.dtos.postservice.EditPostRequestDTO;
import kattsyn.dev.SocialNetwork.exceptions.AppException;
import kattsyn.dev.SocialNetwork.services.PostServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}/post")
@RequiredArgsConstructor
@Tag(name = "Записи")
public class PostController {

    private final PostServiceGrpc postService;

    @Operation(summary = "Получение записи по ID", description = "Возвращает информацию о записи.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) throws AppException {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Operation(summary = "Получение страницы постов")
    @GetMapping("/posts?page={page}&count={count}")
    public ResponseEntity<?> getPosts(@PathVariable int page, @PathVariable int count) throws AppException {
        //todo: разобраться с Mapping'ом
        return ResponseEntity.ok(postService.getPosts(page, count));
    }

    @Operation(summary = "Создание записи", description = "Создает запись.")
    @PostMapping("")
    public ResponseEntity<String> createPost(@RequestBody CreatePostRequestDTO createPostRequestDTO) throws AppException {
            String response = postService.createPost(
                    createPostRequestDTO.getAuthorId(),
                    createPostRequestDTO.getHeader(),
                    createPostRequestDTO.getText());
            return ResponseEntity.ok(response);
    }

    @Operation(summary = "Удаление записи по ID", description = "Возвращает информацию о записи.")
    @DeleteMapping("")
    public ResponseEntity<?> deletePostById(@RequestBody DeletePostRequestDTO requestDTO) throws AppException {
        return ResponseEntity.ok(postService.deletePost(requestDTO));
    }

    @Operation(summary = "Редактирование записи", description = "Возвращает информацию о записи.")
    @PatchMapping("")
    public ResponseEntity<?> editPostById(@RequestBody EditPostRequestDTO requestDTO) throws AppException {
        return ResponseEntity.ok(postService.editPost(requestDTO));
    }
}
