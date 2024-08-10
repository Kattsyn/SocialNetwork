package kattsyn.dev.SocialNetwork.services;

import kattsyn.dev.SocialNetwork.exceptions.AppException;
import kattsyn.dev.SocialNetwork.utils.JwtTokenUtils;
import kattsyn.dev.SocialNetwork.dtos.JwtRequest;
import kattsyn.dev.SocialNetwork.dtos.JwtResponse;
import kattsyn.dev.SocialNetwork.dtos.RegistrationUserDto;
import kattsyn.dev.SocialNetwork.dtos.UserDto;
import kattsyn.dev.SocialNetwork.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public JwtResponse createAuthToken(JwtRequest authRequest) throws AppException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Неправильный логин или пароль");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername()); //достаем UserDetails по username из запроса
        String token = jwtTokenUtils.generateToken(userDetails);
        return new JwtResponse(token);
    }

    @Transactional
    public UserDto createNewUser(RegistrationUserDto registrationUserDto) throws AppException {

        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            //return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"), HttpStatus.BAD_REQUEST);
            throw new AppException(HttpStatus.BAD_REQUEST, "Пароли не совпадают");
        }
        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Пользователь с указанным именем уже существует");
        }
        if (userService.findByEmail(registrationUserDto.getEmail()).isPresent()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Пользователь с указанной почтой уже существует");
        }
        if (userService.findByPhoneNumber(registrationUserDto.getPhoneNumber()).isPresent()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Пользователь с указанным номером телефона уже существует");
        }
        User user = userService.createNewUser(registrationUserDto);
        /*
        Можно сделать разный возврат этого метода:
        1. просто вернуть "окей"
        2. из registrationUserDto собрать UserDetails и сгенерировать JwtToken
        3. вернуть User'а, но тогда через UserDto
         */
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getUsername(),
                user.getBirthDate(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }
}
