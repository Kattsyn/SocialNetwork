package kattsyn.dev.SocialNetwork.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kattsyn.dev.SocialNetwork.services.StatService;
import kattsyn.dev.SocialNetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("${api.path}/stats")
@Tag(name = "Сбор статистики")
@RequiredArgsConstructor
public class StatController {

    private final StatService statService;
    private final UserService userService;

    @Operation(summary = "Дернуть отправку сообщения по кафке")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/sendMessage")
    public void sendMessage() {
        statService.sendMessage();
    }

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
    /*
    @Operation(summary = "Получить количество лайков у поста", description = "Для авторизованных пользователей")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/likes/{postId}")
    public void getLikesByPostId(@PathVariable Long postId) {
        statService.viewPost(postId, userID);
    }

     */
}
