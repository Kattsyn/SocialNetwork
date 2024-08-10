package kattsyn.dev.SocialNetwork.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kattsyn.dev.SocialNetwork.dtos.JwtRequest;
import kattsyn.dev.SocialNetwork.dtos.RegistrationUserDto;
import kattsyn.dev.SocialNetwork.exceptions.AppException;
import kattsyn.dev.SocialNetwork.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path}")
@Tag(name = "Аутентификация", description = "Для аутентификации и регистрации")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth")
    @Operation(summary = "Аутентификация", description = "Аутентификация пользователя по логину и паролю, возвращает JWT")
    //На выходе стоит ResponseEntity<?> потому что на выходе может вернуться что угодно, в т.ч. и ошибка.
    //По хорошему такую логику спрятать в AuthService, а исключения обрабатывать через глобальный перехват исключений
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) throws AppException {
        return ResponseEntity.ok(authService.createAuthToken(authRequest));
    }

    @PostMapping("/registration")
    @Operation(summary = "Регистрация нового пользователя", description = "Создает нового пользователя по логину, паролю и другой информации пользователя.")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) throws AppException {
        return ResponseEntity.ok(authService.createNewUser(registrationUserDto));
    }
}
