package tech.arc9.gateway.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.arc9.gateway.configuration.GatewayConfig;
import tech.arc9.user.UserProto;
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
    }

    public UserServiceProto.GetUserDetailsResponse getUserDetails(
            String userId
    ) {
        return userServiceBlockingStub.getUserDetails(
                UserServiceProto.GetUserDetailsRequest.newBuilder().setUserId(userId).build()
        );
    }

    public UserServiceProto.CreateUserResponse createUser(UserProto.User user) {
        return userServiceBlockingStub.createUser(
                UserServiceProto.CreateUserRequest.newBuilder()
                        .setEmail(user.getEmail())
                        .setFirstName(user.getFirstName())
                        .setLastName(user.getLastName())
                        .setGender(user.getGender())
                        .setBio(user.getBio())
                        .build()
        );
    }

    public UserServiceProto.UpdateUserResponse updateUser(UserProto.User user) {
        return userServiceBlockingStub.updateUser(user);
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
