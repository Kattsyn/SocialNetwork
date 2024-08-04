package kattsyn.dev.SocialNetwork.configs;

import kattsyn.dev.SocialNetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;
    private final PasswordEncoder passwordEncoder;
    @Value("${api.path}")
    private String apiPath;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /*
        JWT - JSON Web Token
        Тк по одному из принципов REST API, наш сервис Stateless. То есть ему все равно что было когда-то,
        каждый запрос самодостаточный, он не зависит от предыдущих запросов.
        Поэтому мы убираем сессии строкой .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
        Но как тогда дальше работать? Нельзя же постоянно при каждом запросе пользователю передавать логин и пароль.
        Поэтому при первом заходе будем генерировать JWT токен, который в себе хранит уже всю необходимую информацию о валидности,
        а также информация о самом пользователе (в том числе о ролях), в том числе время валидности.
        Поэтому в дальнейшем перед любым эндпоинтом (запросом), мы будем сначала направляться на эндпоинт
        для проверки токена, а затем уже на тот, который хотел пользователь.

        Когда пользователь уже авторизованный (имеющий токен) будет пытаться перейти в какой-то эндпоинт, в Http запросе, а точнее
        в Header'е запроса, в поле Authorization будет значение Bearer_<token>
        Header:
            Authorization: Bearer_<token>
        типо такого
         */
        http
                .csrf(CsrfConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                apiPath + "/secured",
                                apiPath + "/users/info",
                                apiPath + "/users/edit").authenticated()
                        .requestMatchers(
                                apiPath + "/admin",
                                apiPath + "/users/edit/{id}",
                                apiPath + "/users",
                                apiPath + "/users/{id}").hasRole("ADMIN")
                        .anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handling -> handling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); //какой фильтр подставляем и перед каким
        return http.build();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
