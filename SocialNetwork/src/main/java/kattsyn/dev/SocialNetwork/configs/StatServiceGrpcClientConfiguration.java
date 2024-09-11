package kattsyn.dev.SocialNetwork.configs;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import kattsyn.dev.grpc.StatServiceGrpc;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatServiceGrpcClientConfiguration {

    @Value("${grpc.client.stat-service.address}")
    private String serverAddress;

    @Bean
    public ManagedChannel statServiceManagedChannel() {
        return ManagedChannelBuilder.forTarget(serverAddress)
                .usePlaintext()
                .build();
    }

    @Bean
    public StatServiceGrpc.StatServiceBlockingStub statServiceBlockingStub(@Qualifier("statServiceManagedChannel") ManagedChannel managedChannel) {
        return StatServiceGrpc.newBlockingStub(managedChannel);
    }
}
