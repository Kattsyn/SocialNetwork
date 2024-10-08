package kattsyn.dev.SocialNetwork.configs;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import kattsyn.dev.grpc.PostServiceGrpc;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostServiceGrpcClientConfiguration {

    @Value("${grpc.client.post-service.address}")
    private String serverAddress;

    @Bean
    public ManagedChannel postServiceManagedChannel() {
        return ManagedChannelBuilder.forTarget(serverAddress)
                .usePlaintext()
                .build();
    }

    @Bean
    public PostServiceGrpc.PostServiceBlockingStub postServiceBlockingStub(@Qualifier("postServiceManagedChannel")ManagedChannel managedChannel) {
        return PostServiceGrpc.newBlockingStub(managedChannel);
    }

}
