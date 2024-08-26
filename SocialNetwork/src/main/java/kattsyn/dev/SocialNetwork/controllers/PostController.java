package kattsyn.dev.SocialNetwork.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kattsyn.dev.SocialNetwork.dtos.postservice.CreatePostRequestDTO;
import kattsyn.dev.SocialNetwork.dtos.postservice.EditPostRequestDTO;
import kattsyn.dev.SocialNetwork.exceptions.AppException;
import kattsyn.dev.SocialNetwork.services.PostServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("${api.path}/posts")
@RequiredArgsConstructor
@Tag(name = "Записи")
public class PostController {

    private final PostServiceGrpc postService;

    @Operation(summary = "Получение записи по ID", description = "Возвращает информацию о записи.")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) throws AppException {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Operation(summary = "Получение страницы постов")
    @SecurityRequirement(name = "JWT")
    @GetMapping("")
    public ResponseEntity<?> getPosts(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int count)
            throws AppException {
        return ResponseEntity.ok(postService.getPosts(page, count));
    }

    @Operation(summary = "Создание записи", description = "Создает запись. Только авторизованным пользователям.")
    @SecurityRequirement(name = "JWT")
    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequestDTO createPostRequestDTO, Principal principal) throws AppException {
        return ResponseEntity.ok(postService.createPost(createPostRequestDTO, principal));
    }

    @Operation(summary = "Удаление записи по ID", description = "Возвращает информацию о записи. Авторизованным пользователям, нужно быть автором поста, либо админом.")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable Long id, Principal principal) throws AppException {
        return ResponseEntity.ok(postService.deletePost(id, principal));
    }

    @Operation(summary = "Редактирование записи", description = "Возвращает информацию о записи. Авторизованным пользователям, нужно быть автором поста, либо админом.")
    @SecurityRequirement(name = "JWT")
    @PatchMapping("")
    public ResponseEntity<?> editPostById(@RequestBody EditPostRequestDTO requestDTO, Principal principal) throws AppException {
        return ResponseEntity.ok(postService.editPost(requestDTO, principal));
    }
}
