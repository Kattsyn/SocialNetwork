package kattsyn.dev.SocialNetwork.testContainers;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import kattsyn.dev.grpc.CreatePostRequest;
import kattsyn.dev.grpc.CreatePostResponse;
import kattsyn.dev.grpc.PostServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.GenericContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class IntegrationTest {

    @Container
    public static final PostgreSQLContainer<?> postgresUserService = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("user_service_db")
            .withUsername("kattsyn")
            .withPassword("katt")
            .waitingFor(Wait.forListeningPort());

    @Container
    private static final PostgreSQLContainer<?> postgresPostService = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("post_service_db")
            .withUsername("kattsyn")
            .withPassword("katt")
            .waitingFor(Wait.forListeningPort());

    @Container
    private static final GenericContainer<?> postServiceContainer = new GenericContainer<>("post-service-image")
            .withExposedPorts(9090)
            .dependsOn(postgresPostService)
            /*
            .withEnv("SPRING_DATASOURCE_URL", postgresPostService.getJdbcUrl())
            .withEnv("SPRING_DATASOURCE_USERNAME", postgresPostService.getUsername())
            .withEnv("SPRING_DATASOURCE_PASSWORD", postgresPostService.getPassword())
            .withEnv("SPRING_DATASOURCE_DRIVER_CLASS_NAME", "org.postgresql.Driver") //todo: удалить если будет успешно подключаться к сервису
             */
            .waitingFor(Wait.forListeningPort());

    @Autowired
    MockMvc mockMvc;

    private static PostServiceGrpc.PostServiceBlockingStub grpcClient;

    @DynamicPropertySource
    static void grpcProperties(DynamicPropertyRegistry registry) {
        /*
        registry.add("spring.datasource.url", postgresPostService::getJdbcUrl);
        registry.add("spring.datasource.username", postgresPostService::getUsername);
        registry.add("spring.datasource.password", postgresPostService::getPassword);
         */

        postServiceContainer.addEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql:latest://" + postgresPostService.getHost() + ":" + postgresPostService.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT) + "/post_service_db");
        postServiceContainer.addEnv("SPRING_DATASOURCE_USERNAME", postgresPostService.getUsername());
        postServiceContainer.addEnv("SPRING_DATASOURCE_PASSWORD", postgresPostService.getPassword());
        postServiceContainer.addEnv("SPRING_DATASOURCE_DRIVER_CLASS_NAME", "org.postgresql.Driver");
        postServiceContainer.start();


        // JDBC URL для PostService
        /*
        registry.add("postservice.datasource.url",
                () -> "jdbc:tc:postgresql:latest://" + postgresPostService.getContainerIpAddress() + ":" + postgresPostService.getMappedPort(5433) + "/post_service_db");
        registry.add("postservice.datasource.username", postgresPostService::getUsername);
        registry.add("postservice.datasource.password", postgresPostService::getPassword);
        registry.add("postservice.datasource.driver-class-name", () -> "org.testcontainers.jdbc.ContainerDatabaseDriver");

         */


        registry.add("spring.datasource.url", () -> "jdbc:tc:postgresql:latest://" + postgresUserService.getContainerIpAddress() + ":" + postgresUserService.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT) + "/user_service_db");
        registry.add("spring.datasource.username", postgresUserService::getUsername);
        registry.add("spring.datasource.password", postgresUserService::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.testcontainers.jdbc.ContainerDatabaseDriver");


        String grpcHost = postServiceContainer.getHost();
        Integer grpcPort = postServiceContainer.getMappedPort(9090);
        registry.add("grpc.host", () -> grpcHost);
        registry.add("grpc.port", grpcPort::toString);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
        grpcClient = PostServiceGrpc.newBlockingStub(channel);


    }

    @Test
    void testUserRegistration() throws Exception {
        String jsonBody = "{" +
                "\"username\":\"testuser\", " +
                "\"password\":\"testpass\"," +
                "\"confirmPassword\":\"testpass\"," +
                "\"name\":\"testName\"," +
                "\"lastname\":\"testLastName\"," +
                "\"birthDate\":\"2003-02-25\"," +
                "\"email\":\"test@gmail.com\"," +
                "\"phoneNumber\":\"+7920202020\"" +
                "}";

        mockMvc.perform(post("/api/v1/registration")
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    void testUserLogin() throws Exception {
        String jsonBody = "{\"username\":\"testuser\", \"password\":\"testpass\"}";

        mockMvc.perform(post("/api/v1/auth")
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isOk()); // Ожидаем код 200
    }

    @Test
    void createPostTest() {
        CreatePostRequest request = CreatePostRequest.newBuilder()
                .setAuthorId(1L)
                .setHeader("testHeader")
                .setPostContent("testContent")
                .build();

        CreatePostResponse response = grpcClient.createPost(request).next();

        assertThat(response).isNotNull();
    }
}
