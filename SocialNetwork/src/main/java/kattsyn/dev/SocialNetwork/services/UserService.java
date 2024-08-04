package kattsyn.dev.SocialNetwork.services;

import kattsyn.dev.SocialNetwork.dtos.RegistrationUserDto;
import kattsyn.dev.SocialNetwork.dtos.UserDto;
import kattsyn.dev.SocialNetwork.entities.User;
import kattsyn.dev.SocialNetwork.exceptions.AppError;
import kattsyn.dev.SocialNetwork.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));
        return new org.springframework.security.core.userdetails.User( //преобразовываем нашего User в User, понятный спрингу
                user.getUsername(), //собираем username
                user.getPassword(), //собираем пароль
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
                //преобразовываем наши роли в роли, понятные спрингу
        );
    }

    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User();
        user.setUsername(registrationUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setName(registrationUserDto.getName());
        user.setLastName(registrationUserDto.getLastName());
        user.setEmail(registrationUserDto.getEmail());
        user.setBirthDate(registrationUserDto.getBirthDate());
        user.setPhoneNumber(registrationUserDto.getPhoneNumber());
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);
    }

    public ResponseEntity<?> changeUserData(Optional<User> user, UserDto userDto) {
        if (user.isPresent()) {
            if (userDto.getName() != null) {
                user.get().setName(userDto.getName());
            }
            if (userDto.getLastName() != null) {
                user.get().setLastName(userDto.getLastName());
            }
            if (userDto.getLastName() != null) {
                user.get().setLastName(userDto.getLastName());
            }
            if (userDto.getBirthDate() != null) {
                user.get().setBirthDate(userDto.getBirthDate());
            }
            if (userDto.getEmail() != null) {
                user.get().setEmail(userDto.getEmail());
            }
            if (userDto.getPhoneNumber() != null) {
                user.get().setPhoneNumber(userDto.getPhoneNumber());
            }
            return new ResponseEntity<>(save(user.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "User not found"), HttpStatus.BAD_REQUEST);
        }
    }
}
