package kattsyn.dev.SocialNetwork.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kattsyn.dev.SocialNetwork.exceptions.AppException;
import kattsyn.dev.SocialNetwork.services.StatService;
import kattsyn.dev.SocialNetwork.services.StatServiceGrpc;
import kattsyn.dev.SocialNetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("${api.path}/stats")
@Tag(name = "Сбор статистики")
@RequiredArgsConstructor
public class StatController {

    private final StatService statService;
    private final UserService userService;
    private final StatServiceGrpc statServiceGrpc;

    @Operation(summary = "Поставить лайк посту", description = "Для авторизованных пользователей")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/like/{postId}")
    public void likePost(@PathVariable Long postId, Principal principal) {
        Long userID = userService.findByUsername(principal.getName()).get().getId();
        statService.likePost(postId, userID);
    }

    @Operation(summary = "Засчитать просмотр посту", description = "Для авторизованных пользователей")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/view/{postId}")
    public void viewPost(@PathVariable Long postId, Principal principal) {
        Long userID = userService.findByUsername(principal.getName()).get().getId();
        statService.viewPost(postId, userID);
    }

    @Operation(summary = "Получить N лучших постов по лайкам", description = "Для авторизованных пользователей")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/postsByLikes/{N}")
    public ResponseEntity<?> getNPostsByLikes(@PathVariable int N) throws AppException {
        return ResponseEntity.ok(statServiceGrpc.getNPostsByLikes(N));
    }
    @Operation(summary = "Получить N лучших постов по просмотрам", description = "Для авторизованных пользователей")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/postsByViews/{N}")
    public ResponseEntity<?> getNPostsByViews(@PathVariable int N) throws AppException {
        return ResponseEntity.ok(statServiceGrpc.getNPostsByViews(N));
    }

    @Operation(summary = "Получить N лучших авторов по лайкам", description = "Для авторизованных пользователей")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/authorsByLikes/{N}")
    public ResponseEntity<?> getNAuthorsByLikes(@PathVariable int N) throws AppException {
        return ResponseEntity.ok(statServiceGrpc.getNAuthorsByLikes(N));
    }

    @Operation(summary = "Получить N лучших авторов по просмотрам", description = "Для авторизованных пользователей")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/authorsByViews/{N}")
    public ResponseEntity<?> getNAuthorsByViews(@PathVariable int N) throws AppException {
        return ResponseEntity.ok(statServiceGrpc.getNAuthorsByViews(N));
    }


    @Operation(summary = "Получить кол-во лайков поста по его ID", description = "Для авторизованных пользователей")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/postLikesById/{postId}")
    public ResponseEntity<Integer> getLikesById(@PathVariable Long postId) throws AppException {
        return ResponseEntity.ok(statServiceGrpc.getLikesById(postId));
    }

    @Operation(summary = "Получить кол-во просмотров поста по его ID", description = "Для авторизованных пользователей")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/postViewsById/{postId}")
    public ResponseEntity<Integer> getViewsById(@PathVariable Long postId) throws AppException {
        return ResponseEntity.ok(statServiceGrpc.getViewsById(postId));
    }
}
