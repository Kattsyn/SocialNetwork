package kattsyn.dev.SocialNetwork.services;


import kattsyn.dev.SocialNetwork.dtos.RegistrationUserDto;
import kattsyn.dev.SocialNetwork.dtos.UserDto;
import kattsyn.dev.SocialNetwork.entities.Role;
import kattsyn.dev.SocialNetwork.entities.User;
import kattsyn.dev.SocialNetwork.exceptions.AppException;
import kattsyn.dev.SocialNetwork.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
    }

    @Test
    void getUsersByIdList_validIdList_ShouldReturnUserList() {
        List<Long> idList = new ArrayList<>();
        idList.add(1L);
        idList.add(2L);

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("first_user");
        user1.setPassword("first_user_pass");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("second_user");
        user2.setPassword("second_user_pass");

        List<User> expectedList = new ArrayList<>();
        expectedList.add(user1);
        expectedList.add(user2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        List<User> resultList = userService.getUsersByIdList(idList);

        assertThat(resultList).isEqualTo(expectedList);

        verify(userRepository, times(2)).findById(any());
    }

    @Test
    void getUsersByIdList_invalidIdList_ShouldReturnNullUserList() {
        List<Long> idList = new ArrayList<>();
        idList.add(1L);
        idList.add(2L);

        List<User> expectedList = new ArrayList<>();
        expectedList.add(null);
        expectedList.add(null);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        List<User> resultList = userService.getUsersByIdList(idList);

        assertThat(resultList).isEqualTo(expectedList);

        verify(userRepository, times(2)).findById(any());
    }

    @Test
    void loadUserByUsername_validUsername_ShouldReturnUserDetails() {
        Role role = new Role();
        role.setName("ROLE_USER");
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        String username = "testUsername";
        String password = "testPassword";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(roleList);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }

    @Test
    void loadUserByUsername_invalidUsername_shouldThrowUsernameNotFoundException() {
        String username = "username";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Пользователь 'username' не найден");
    }

    @Test
    void changeUser_validUser_ShouldSaveNewUserInfo() throws AppException {
        Optional<User> user = Optional.of(new User());
        UserDto userDto = new UserDto(
                1L,
                "name",
                "surname",
                "username",
                LocalDate.now(),
                "ivan@mail.ru",
                "+788292");

        when(userRepository.save(user.get())).thenReturn(user.get());

        assertThat(userService.changeUserData(user, userDto)).isEqualTo(user.get());

        verify(userRepository, times(1)).save(user.get());
    }

    @Test
    void changeUser_userOptionalIsEmpty_ShouldThrowAppException() {
        Optional<User> user = Optional.empty();
        UserDto userDto = new UserDto(
                1L,
                "name",
                "surname",
                "username",
                LocalDate.now(),
                "ivan@mail.ru",
                "+788292");

        assertThatThrownBy(() -> userService.changeUserData(user, userDto))
                .isInstanceOf(AppException.class)
                .hasMessageContaining("Пользователь не найден");

        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_validUser_shouldReturnDeletedUser() throws AppException {
        Optional<User> user = Optional.of(new User());
        user.get().setUsername("testUserUsername");

        assertThat(userService.deleteUser(user)).isEqualTo(user.get());

        verify(userRepository, times(1)).delete(user.get());
    }

    @Test
    void deleteUser_invalidUser_shouldThrowAppException() {
        Optional<User> userOptional = Optional.empty();  // Пользователь не найден

        assertThatThrownBy(() -> userService.deleteUser(userOptional))
                .isInstanceOf(AppException.class)
                .hasMessageContaining("Пользователь не найден");

        verify(userRepository, never()).delete(any());
    }

    @Test
    void createNewUser_validUser_shouldReturnSavedUser() {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto();
        registrationUserDto.setUsername("testUser");
        registrationUserDto.setPassword("rawPassword");

        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        when(roleService.getUserRole()).thenReturn(userRole);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setUsername("testUser");
        savedUser.setPassword("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User createdUser = userService.createNewUser(registrationUserDto);

        assertThat(createdUser.getUsername()).isEqualTo("testUser");
        assertThat(createdUser.getPassword()).isEqualTo("encodedPassword");

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("rawPassword");
    }
}
