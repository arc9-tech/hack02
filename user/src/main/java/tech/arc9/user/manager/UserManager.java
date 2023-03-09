package tech.arc9.user.manager;

import org.springframework.stereotype.Component;
import tech.arc9.user.UserProto;
import tech.arc9.user.UserServiceProto;

@Component
public class UserManager {



    public UserServiceProto.GetUserDetailsResponse
            getUserDetails(UserServiceProto.GetUserDetailsRequest req) {

        //TODO: get from redis cache
        //TODO: send 404 if does not exist

        UserProto.User.Builder userBuilder = UserProto.User.newBuilder().setId(req.getUserId())
                .setEmail("tariq.amtoly@gmail.com")
                .setName("Tariqul Islam");
        UserServiceProto.GetUserDetailsResponse.Builder responseBuilder = UserServiceProto.GetUserDetailsResponse.newBuilder()
                .setResponseCode(200)
                .setResponseMessage("User found")
                .setUser(userBuilder.build());
        return responseBuilder.build();

    }

}
