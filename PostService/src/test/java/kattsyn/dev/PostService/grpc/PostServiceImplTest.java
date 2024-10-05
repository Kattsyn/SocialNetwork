package kattsyn.dev.PostService.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import kattsyn.dev.grpc.PostServiceGrpc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class PostServiceImplTest {

    @Container
    public static final PostgreSQLContainer<?> postgresPostService = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("post-service-db")
            .withUsername("kattsyn")
            .withPassword("katt")
            .waitingFor(Wait.forListeningPort());


    private static PostServiceGrpc.PostServiceBlockingStub grpcClient;

    @DynamicPropertySource
    static void grpcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:tc:postgresql:latest://" + postgresPostService.getContainerIpAddress() + ":" + postgresPostService.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT) + "/post_service_db");
        registry.add("spring.datasource.username", postgresPostService::getUsername);
        registry.add("spring.datasource.password", postgresPostService::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.testcontainers.jdbc.ContainerDatabaseDriver");
/*
        String grpcHost = postgresPostService.getHost();
        Integer grpcPort = postgresPostService.getMappedPort(9090);
        registry.add("grpc.host", () -> grpcHost);
        registry.add("grpc.port", grpcPort::toString);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
        grpcClient = PostServiceGrpc.newBlockingStub(channel);

 */
    }

    @Test
    void test() {
        assertThat(1).isEqualTo(1);
    }
}
