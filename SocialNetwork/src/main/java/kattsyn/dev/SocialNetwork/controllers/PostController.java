package kattsyn.dev.SocialNetwork.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kattsyn.dev.SocialNetwork.dtos.postservice.PostDTO;
import kattsyn.dev.SocialNetwork.services.PostServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public String getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @Operation(summary = "Создание записи", description = "Создает запись.")
    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody PostDTO postDTO) {
        try {
            String response = postService.createPost(
                    postDTO.getAuthorId(),
                    postDTO.getHeader(),
                    postDTO.getText());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Обработка ошибок
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("PostService is unavailable: " + e.getMessage());
        }
    }
}
