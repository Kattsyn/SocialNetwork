package kattsyn.dev.SocialNetwork.services;

import kattsyn.dev.SocialNetwork.utils.JwtTokenUtils;
import kattsyn.dev.SocialNetwork.dtos.JwtRequest;
import kattsyn.dev.SocialNetwork.dtos.JwtResponse;
import kattsyn.dev.SocialNetwork.dtos.RegistrationUserDto;
import kattsyn.dev.SocialNetwork.dtos.UserDto;
import kattsyn.dev.SocialNetwork.entities.User;
import kattsyn.dev.SocialNetwork.exceptions.AppError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    //На выходе стоит ResponseEntity<?> потому что на выходе может вернуться что угодно, в т.ч. и ошибка.
    //По хорошему такую логику спрятать в AuthService, а исключения обрабатывать через глобальный перехват исключений
    public ResponseEntity<?> createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername()); //достаем UserDetails по username из запроса
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с указанным именем уже существует"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findByEmail(registrationUserDto.getEmail()).isPresent()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с указанной почтой уже существует"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findByPhoneNumber(registrationUserDto.getPhoneNumber()).isPresent()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с указанным номером телефона уже существует"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto);
        /*
        Можно сделать разный возврат этого метода:
        1. просто вернуть "окей"
        2. из registrationUserDto собрать UserDetails и сгенерировать JwtToken
        3. вернуть User'а, но тогда через UserDto
         */
        return ResponseEntity.ok(new UserDto(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getUsername(),
                user.getBirthDate(),
                user.getEmail(),
                user.getPhoneNumber()
        ));
    }
}
