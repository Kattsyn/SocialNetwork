package kattsyn.dev.SocialNetwork;

import com.google.api.Http;
import kattsyn.dev.SocialNetwork.dtos.JwtRequest;
import kattsyn.dev.SocialNetwork.dtos.JwtResponse;
import kattsyn.dev.SocialNetwork.dtos.RegistrationUserDto;
import kattsyn.dev.SocialNetwork.dtos.UserDto;
import kattsyn.dev.SocialNetwork.dtos.postservice.CreatePostRequestDTO;
import kattsyn.dev.SocialNetwork.dtos.postservice.EditPostRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String jwtTokenUser1;
    private String jwtTokenUser2;
    private List<String> messages;
    private String requestUrl;

    @BeforeAll
    public void setUp() {
        registerAndAuthenticate("user1", "password1", "testuser1@mail.ru", "+7777777");
        jwtTokenUser1 = authenticate("user1", "password1");

        registerAndAuthenticate("user2", "password2", "testuser2@mail.ru", "+9999999");
        jwtTokenUser2 = authenticate("user2", "password2");

        messages = new ArrayList<>();
        requestUrl = "http://localhost:" + port + "/api/v1";
    }

    private void registerAndAuthenticate(String username, String password, String email, String phoneNumber) {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto();
        registrationUserDto.setUsername(username);
        registrationUserDto.setPassword(password);
        registrationUserDto.setConfirmPassword(password);
        registrationUserDto.setEmail(email);
        registrationUserDto.setPhoneNumber(phoneNumber);
        ResponseEntity<UserDto> registrationResponse = restTemplate.postForEntity("/api/v1/registration", registrationUserDto, UserDto.class);
        assertThat(registrationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private String authenticate(String username, String password) {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername(username);
        jwtRequest.setPassword(password);
        ResponseEntity<JwtResponse> authenticationResponse = restTemplate.postForEntity("/api/v1/auth", jwtRequest, JwtResponse.class);
        assertThat(authenticationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        return authenticationResponse.getBody().getToken();
    }


    void authTest() {
        String username = "user1";
        String password = "password1";

        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername(username);
        jwtRequest.setPassword(password);
        ResponseEntity<JwtResponse> authenticationResponse = restTemplate.postForEntity(requestUrl + "/auth", jwtRequest, JwtResponse.class);
        assertThat(authenticationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @WithMockUser(username = "user1")
    public void testUser1CreatesPost() {
        CreatePostRequestDTO request = new CreatePostRequestDTO();
        request.setHeader("PostHeader");
        request.setPostContent("SuperInterestingPostContent");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenUser1);

        HttpEntity<CreatePostRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl + "/posts", entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @WithMockUser(username = "user1")
    public void testUser1GetsCreatedPost() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenUser1);

        ResponseEntity<String> response = restTemplate.exchange(requestUrl + "/posts/1", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().contains("SuperInterestingPostContent")).isTrue();
    }

    @Test
    @WithMockUser(username = "user2")
    public void testUser2TriesToEditUser1Post() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenUser2);

        EditPostRequestDTO request = new EditPostRequestDTO();
        request.setHeader("NewHeader");
        request.setPostContent("NewPostContent");

        HttpEntity<EditPostRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(requestUrl + "/posts/{id}", HttpMethod.PATCH, entity, String.class, 1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @WithMockUser(username = "user1")
    public void testUser1TriesToEditHisPost() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenUser1);

        EditPostRequestDTO request = new EditPostRequestDTO();
        request.setHeader("NewHeader");
        request.setPostContent("NewPostContent");

        HttpEntity<EditPostRequestDTO> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(requestUrl + "/posts/{id}", HttpMethod.PATCH, entity, String.class, 1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @WithMockUser(username = "user1")
    public void testUser1LikesPost() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenUser1);

        ResponseEntity<Void> likeResponse = restTemplate.exchange(requestUrl + "/stats/like/1", HttpMethod.POST, new HttpEntity<>(headers), Void.class);
        assertThat(likeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @WithMockUser(username = "user1")
    public void user1GetsLikesForPost1() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenUser1);

        ResponseEntity<Integer> likesByIdResponse = restTemplate.exchange(requestUrl + "/stats/postLikesById/{id}", HttpMethod.GET, new HttpEntity<>(headers), Integer.class, 1);
        assertThat(likesByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(likesByIdResponse.getBody()).isEqualTo(1);
    }


}
