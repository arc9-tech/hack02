package tech.arc9.gateway.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.arc9.gateway.configuration.GatewayConfig;
import tech.arc9.user.UserServiceGrpc;
import tech.arc9.user.UserServiceProto;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

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
//                .withDeadlineAfter(5, TimeUnit.SECONDS);
    }

    public UserServiceProto.GetUserDetailsResponse getUserDetails(
            String userId
    ) {
        return userServiceBlockingStub.getUserDetails(
                UserServiceProto.GetUserDetailsRequest.newBuilder().setUserId(userId).build()
        );
    }

    public UserServiceProto.CreateUserResponse createUser(String name, String email) {
        return userServiceBlockingStub.createUser(
                UserServiceProto.CreateUserRequest.newBuilder().setEmail(email == null ?"":email)
                        .setName(name == null ? "":name).build()
        );
    }

    public UserServiceProto.UpdateUserResponse updateUser(String id, String name, String email) {
        return userServiceBlockingStub.updateUser(
                UserServiceProto.UpdateUserRequest.newBuilder()
                        .setId(id == null ? "" : id)
                        .setEmail(email == null ?"":email)
                        .setName(name == null ? "":name).build()
        );
    }

    public UserServiceProto.GetUserListResponse getUserList(int limit, int offset) {
        return userServiceBlockingStub.getUserList(
                UserServiceProto.GetUserListRequest.newBuilder()
                        .setLimit(limit)
                        .setOffset(offset)
                        .build()
        );
    }

}
