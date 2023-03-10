package tech.arc9.user.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import tech.arc9.user.UserProto;
import tech.arc9.user.UserServiceProto;
import tech.arc9.user.db.mysql.entity.UserEntity;
import tech.arc9.user.db.mysql.repository.UserRepository;
import tech.arc9.user.db.mysql.tool.OffsetBasedPageRequest;

import javax.annotation.PostConstruct;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class UserManager {


//    private private static final int  = 582; UserRepository userRepository;
    private final UserRepository userRepository;

    public UserManager(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
        userRepository.findById("");
    }

    Logger log = LoggerFactory.getLogger(UserManager.class);




//    @PostConstruct
//    private void test() {
//        log.info("Hello i am ready");
//        UserEntity entity = userRepository.findById("ghujgyh").orElse(null);
//        log.info("found {}", entity);
//        entity = userRepository.findById("bf2c3dd2-e4ee-4146-a2ed-253edf86fb6b").orElse(null);
//        log.info("found {}", entity);
//    }

    public UserServiceProto.GetUserDetailsResponse
    getUserDetails(UserServiceProto.GetUserDetailsRequest req) {

//        log.info("Request recieved {}", req);


        if(req.getUserId().isEmpty()) {
            return UserServiceProto.GetUserDetailsResponse.newBuilder()
                    .setResponseCode(400)
                    .setResponseMessage("Empty id not allowed").build();
        }
        //TODO: get from redis cache
        UserEntity user = null;
        try {
            user = userRepository.findById(req.getUserId()).orElse(null);
        } catch (Exception e) {
            log.error("database error", e);
        }
        if(user == null) {
            return UserServiceProto.GetUserDetailsResponse.newBuilder()
                    .setResponseCode(404)
                    .setResponseMessage("User not found").build();
        }
        UserProto.User.Builder userBuilder = UserProto.User.newBuilder().setId(req.getUserId())
                .setEmail(user.email)
                .setName(user.name);
        UserServiceProto.GetUserDetailsResponse.Builder responseBuilder = UserServiceProto.GetUserDetailsResponse.newBuilder()
                .setResponseCode(200)
                .setResponseMessage("User found")
                .setUser(userBuilder.build());
        return responseBuilder.build();

    }


    public UserServiceProto.GetUserListResponse getUserList(UserServiceProto.GetUserListRequest req) {
        int limit = req.getLimit();
        int offset = req.getOffset();
        if(limit <= 0 || offset < 0) {
            return UserServiceProto.GetUserListResponse.newBuilder()
                    .setResponseCode(400)
                    .setResponseMessage("limit <= 0 or offset < 0 not allowed").build();
        }

        List<UserEntity> userEntityList = userRepository.findAll(
                OffsetBasedPageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "name"))
        ).toList();

        UserServiceProto.GetUserListResponse.Builder responseBuilder = UserServiceProto.GetUserListResponse.newBuilder();
        for(UserEntity entity: userEntityList) {
            responseBuilder.addUsers(UserProto.User.newBuilder().setEmail(entity.email)
                    .setName(entity.name)
                    .setId(entity.id).build());
        }
        responseBuilder.setResponseCode(200);
        responseBuilder.setResponseMessage("User list found");
        return responseBuilder.build();
    }
    private static boolean isValidEmailAddress(String email) {
        boolean isValid = false;
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            isValid = true;
        } catch (AddressException e) {
            // email is not valid
        }
        return isValid;
    }
    public UserServiceProto.CreateUserResponse createUser(UserServiceProto.CreateUserRequest req) {
        //validation
        String name = req.getName();
        String email = req.getEmail();
        if(!isValidEmailAddress(email)) {
            return UserServiceProto.CreateUserResponse.newBuilder()
                    .setResponseCode(400)
                    .setResponseMessage("invalid email").build();
        }
        if(name.isEmpty()) {
            return UserServiceProto.CreateUserResponse.newBuilder()
                    .setResponseCode(400)
                    .setResponseMessage("name cant be empty").build();
        }

        UserEntity entity = new UserEntity();
        entity.id = UUID.randomUUID().toString();
        entity.name = name;
        entity.email = email;
        entity.createTime = Instant.now();
        entity.lastUpdateTime = Instant.now();
        entity.version = 0;
        userRepository.save(entity);
        //Put in cache
        return UserServiceProto.CreateUserResponse.newBuilder()
                .setResponseCode(200)
                .setResponseMessage("User Created")
                .setUser(
                        UserProto.User.newBuilder()
                                .setId(entity.id)
                                .setName(entity.name)
                                .setEmail(entity.email)
                ).build();

    }

    public UserServiceProto.UpdateUserResponse updateUser(UserServiceProto.UpdateUserRequest req) {
        //validation
        String name = req.getName();
        String email = req.getEmail();
        UserEntity entity = null;
        try {
            entity = userRepository.findById(req.getId()).orElse(null);
        } catch (Exception e) {
            //pass
        }
        if(entity == null) {
            return UserServiceProto.UpdateUserResponse.newBuilder()
                    .setResponseCode(404)
                    .setResponseMessage("User not found").build();
        }

        if(!name.isEmpty())
            entity.name = name;
        if(!email.isEmpty())
            entity.email = email;
        entity.lastUpdateTime = Instant.now();
        entity.version++;

        try {
            userRepository.save(entity);
        } catch (Exception e) {
            log.error("Error while updating user", e);
            return UserServiceProto.UpdateUserResponse.newBuilder()
                    .setResponseCode(500)
                    .setResponseMessage("Database error: " + e.getMessage()).build();
        }

        //Put in cache
        return UserServiceProto.UpdateUserResponse.newBuilder()
                .setResponseCode(200)
                .setResponseMessage("User Created")
                .setUser(
                        UserProto.User.newBuilder()
                                .setId(entity.id)
                                .setName(entity.name)
                                .setEmail(entity.email)
                ).build();

    }

}
