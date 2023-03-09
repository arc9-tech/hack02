package tech.arc9.gateway.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.arc9.gateway.configuration.GatewayConfig;
import tech.arc9.user.UserServiceGrpc;
import tech.arc9.user.UserServiceProto;

import javax.annotation.PostConstruct;

@Component
public class UserServiceClient {

    @Autowired private GatewayConfig config;
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    @PostConstruct
    private void initialize() {
        userServiceBlockingStub = UserServiceGrpc.newBlockingStub(
                ManagedChannelBuilder.forAddress(
                        config.getUserServiceHost(),
                        config.getUserServicePort()
                ).usePlaintext().build()
        );
    }

    public UserServiceProto.GetUserDetailsResponse getUserDetails(
            String userId
    ) {
        return userServiceBlockingStub.getUserDetails(
                UserServiceProto.GetUserDetailsRequest.newBuilder().setUserId(userId).build()
        );
    }
}
