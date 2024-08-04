package kattsyn.dev.SocialNetwork.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class JwtRequest {
    @Schema(description = "Логин пользователя", example = "ivan123")
    private String username;
    @Schema(description = "Пароль пользователя", example = "secret_pass")
    private String password;
}
