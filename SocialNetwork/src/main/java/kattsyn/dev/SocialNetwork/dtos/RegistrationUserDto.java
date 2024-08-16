package kattsyn.dev.SocialNetwork.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistrationUserDto {
    @Schema(description = "Логин пользователя", example = "ivan123")
    private String username;
    @Schema(description = "Пароль пользователя", example = "secret_pass")
    private String password;
    @Schema(description = "Повторный ввод пароля пользователя", example = "secret_pass")
    private String confirmPassword;

    @Schema(description = "Имя пользователя", example = "Ivan")
    private String name;
    @Schema(description = "Фамилия пользователя", example = "Ivanov")
    private String lastName;
    @Schema(description = "Дата рождения пользователя формата yyyy-mm-dd", example = "2003-02-25")
    private LocalDate birthDate;
    @Schema(description = "Адрес электронной почты пользователя", example = "ivan123@gmail.com")
    private String email;
    @Schema(description = "Номер телефона пользователя", example = "+79209992211")
    private String phoneNumber;
}
