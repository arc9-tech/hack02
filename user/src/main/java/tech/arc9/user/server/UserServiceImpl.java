package tech.arc9.user.server;


import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.arc9.user.UserServiceGrpc;
import tech.arc9.user.UserServiceProto;
import tech.arc9.user.manager.UserManager;


@Service
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired private UserManager userManager;


    @Override
    public void getUserDetails(UserServiceProto.GetUserDetailsRequest request,
                               StreamObserver<UserServiceProto.GetUserDetailsResponse> responseObserver) {
        responseObserver.onNext(userManager.getUserDetails(request));
        responseObserver.onCompleted();
    }

    @Override
    public void createUser(UserServiceProto.CreateUserRequest request,
                           StreamObserver<UserServiceProto.CreateUserResponse> responseObserver) {
        super.createUser(request, responseObserver);
    }

    @Override
    public void updateUser(UserServiceProto.UpdateUserRequest request,
                           StreamObserver<UserServiceProto.UpdateUserResponse> responseObserver) {
        super.updateUser(request, responseObserver);
    }

    @Override
    public void getUserList(UserServiceProto.GetUserListRequest request,
                            StreamObserver<UserServiceProto.GetUserListResponse> responseObserver) {
        super.getUserList(request, responseObserver);
    }
}
