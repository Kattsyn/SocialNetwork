package kattsyn.dev.SocialNetwork.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.*;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenUtils Tests")
class JwtTokenUtilsTests {

    @InjectMocks
    private JwtTokenUtils jwtTokenUtils;
    @Mock
    private UserDetails userDetails;
    @Mock
    private List<GrantedAuthority> authorities;

    private String secret;
    private Duration jwtLifeTime;


    @BeforeEach
    void setUp() {
        jwtTokenUtils = new JwtTokenUtils();

        secret = "984hg493gh0439rthr0429uruj2309yh937gc763fe87t3f89723gf";
        jwtLifeTime = Duration.ofMinutes(30);

        authorities = new ArrayList<>();

        try {
            java.lang.reflect.Field secretField = JwtTokenUtils.class.getDeclaredField("secret");
            secretField.setAccessible(true);
            secretField.set(jwtTokenUtils, secret);

            java.lang.reflect.Field jwtLifetimeField = JwtTokenUtils.class.getDeclaredField("jwtLifeTime");
            jwtLifetimeField.setAccessible(true);
            jwtLifetimeField.set(jwtTokenUtils, jwtLifeTime);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @Test
    @DisplayName("testGenerateToken_WithValidUserDetails")
    public void testGenerateToken_WithValidUserDetails_ShouldReturnValidToken() {

        when(userDetails.getUsername()).thenReturn("testUser");
        doReturn(authorities).when(userDetails).getAuthorities();

        GrantedAuthority authority = Mockito.mock(GrantedAuthority.class);
        when(authority.getAuthority()).thenReturn("ROLE_USER");
        authorities.add(authority);

        String token = jwtTokenUtils.generateToken(userDetails);

        assertThat(token).isNotNull();

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret).build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo("testUser");
        assertThat(((List<?>) claims.get("roles")).size()).isEqualTo(1);
        assertThat(((List<?>) claims.get("roles")).get(0)).isEqualTo("ROLE_USER");
    }
}
