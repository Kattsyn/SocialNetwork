package kattsyn.dev.SocialNetwork.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kattsyn.dev.SocialNetwork.dtos.UserDto;
import kattsyn.dev.SocialNetwork.entities.User;
import kattsyn.dev.SocialNetwork.exceptions.AppException;
import kattsyn.dev.SocialNetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path}/users")
@Tag(name = "Пользователи")
public class UserController {
    private final UserService userService;

    //todo: добавить удаление пользователя для авторизованного пользователя

    @GetMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение информации о всех пользователях", description = "Возвращает список пользователей. Только для админов.")
    public List<User> users() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение информации о пользователе", description = "Возвращает информацию о пользователе по ID. Только для админов.")
    public Optional<User> findById(@PathVariable Long id) throws AppException {
        return userService.findById(id);
    }

    @GetMapping("/info")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение информации о пользователе", description = "Возвращает информацию об авторизованном пользователе. Для всех авторизованных.")
    public Optional<User> findCurrentUser(Principal principal) {
        return userService.findByUsername(principal.getName());
    }

    @PatchMapping("/edit/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Изменение информации пользователя", description = "Изменяет информацию о пользователе по ID. Только для админов.")
    public ResponseEntity<?> changeUserDataById(@PathVariable Long id, @RequestBody UserDto userDto) throws AppException {
        return new ResponseEntity<>(userService.changeUserData(userService.findById(id), userDto), HttpStatus.OK);
    }

    @PatchMapping("/edit")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Изменение информации пользователя", description = "Изменяет информацию авторизованного пользователя. Для всех авторизованных.")
    public ResponseEntity<?> changeCurrentUserData(@RequestBody UserDto userDto, Principal principal) throws AppException {
        return new ResponseEntity<>(userService.changeUserData(userService.findByUsername(principal.getName()), userDto), HttpStatus.OK);
    }
}
