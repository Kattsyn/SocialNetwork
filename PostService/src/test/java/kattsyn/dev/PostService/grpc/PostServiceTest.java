package kattsyn.dev.PostService.grpc;

import io.grpc.Server;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    private final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private PostServiceImpl postServiceImpl;
    private Server server;
    private String serverName;

    @BeforeEach
    public void setUp() throws Exception {
        postServiceImpl = new PostServiceImpl();
        serverName = InProcessServerBuilder.generateName();

        server = grpcCleanup.register(
                InProcessServerBuilder.forName(serverName)
                        .addService(postServiceImpl) // Добавление сервиса
                        .directExecutor() // Используем прямой executor
                        .build()
        );
        server.start();
    }

    @AfterEach
    public void tearDown() {
        grpcCleanup.close();
    }

    @Test
    public void testYourGrpcMethod() {
        var channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName)
                        .directExecutor()
                        .build()
        );

        var stub = YourServiceGrpc.newBlockingStub(channel);
        var request = YourRequest.newBuilder().setName("World").build();
        var response = stub.yourGrpcMethod(request);

        assertThat(response.getMessage()).isEqualTo("Hello World");
    }
}
