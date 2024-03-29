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
import tech.arc9.user.db.redis.dao.RedisDao;
import tech.arc9.user.model.User;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class UserManager {


    Logger log = LoggerFactory.getLogger(UserManager.class);
    private final UserRepository userRepository;
    @Autowired private RedisDao redisDao;
    private static final String REDIS_KEY_PREFIX_USER = "USER:";


    public UserManager(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
        userRepository.findById("");
    }



    public UserServiceProto.GetUserDetailsResponse
    getUserDetails(UserServiceProto.GetUserDetailsRequest req) {

        if(req.getUserId().isEmpty()) {
            return UserServiceProto.GetUserDetailsResponse.newBuilder()
                    .setResponseCode(400)
                    .setResponseMessage("Empty id not allowed").build();
        }
        //TODO: get from redis cache
        User model = null;
        try {

            model = (User)redisDao.get(REDIS_KEY_PREFIX_USER + req.getUserId());
            if(model == null) {
                UserEntity entity = userRepository.findById(req.getUserId()).orElse(null);
                if(entity != null) {
                    model = new User(entity);
                    log.info("saving to redis cache");
                    redisDao.set(REDIS_KEY_PREFIX_USER+req.getUserId(), model);
                }
            } else {
                log.info("Retrived from cache");
            }

        } catch (Exception e) {
            log.error("database error", e);
        }
        if(model == null) {
            return UserServiceProto.GetUserDetailsResponse.newBuilder()
                    .setResponseCode(404)
                    .setResponseMessage("User not found").build();
        }
        UserServiceProto.GetUserDetailsResponse.Builder responseBuilder = UserServiceProto.GetUserDetailsResponse.newBuilder()
                .setResponseCode(200)
                .setResponseMessage("User found")
                .setUser(model.toProto());
        return responseBuilder.build();

    }


    public UserServiceProto.GetUserListResponse getUserList(UserServiceProto.GetUserListRequest req) {

        log.info("Request recieved getUserList {}", req);
        int limit = req.getLimit();
        int offset = req.getOffset();
        if(limit <= 0 || offset < 0) {
            return UserServiceProto.GetUserListResponse.newBuilder()
                    .setResponseCode(400)
                    .setResponseMessage("limit <= 0 or offset < 0 not allowed").build();
        }

        List<UserEntity> userEntityList = userRepository.findAll(
                OffsetBasedPageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "firstName", "lastName"))
        ).toList();

        UserServiceProto.GetUserListResponse.Builder responseBuilder = UserServiceProto.GetUserListResponse.newBuilder();
        for(UserEntity entity: userEntityList) {
            responseBuilder.addUsers(UserProto.User.newBuilder()
                    .setId(entity.id)
                    .setEmail(entity.email)
                    .setFirstName(entity.firstName == null ? "" : entity.firstName)
                    .setLastName(entity.lastName == null ? "" : entity.lastName)
                    .setGender(entity.gender == null ? "" : entity.gender)
                    .setBio(entity.bio == null ? "" : entity.bio)
                    .build());
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
//        String name = req.getName();
//        String email = req.getEmail();
        if(!isValidEmailAddress(req.getEmail())) {
            return UserServiceProto.CreateUserResponse.newBuilder()
                    .setResponseCode(400)
                    .setResponseMessage("invalid email").build();
        }
        if(req.getFirstName().isEmpty()) {
            return UserServiceProto.CreateUserResponse.newBuilder()
                    .setResponseCode(400)
                    .setResponseMessage("FirstName cant be empty").build();
        }

        if(req.getLastName().isEmpty()) {
            return UserServiceProto.CreateUserResponse.newBuilder()
                    .setResponseCode(400)
                    .setResponseMessage("LastName cant be empty").build();
        }

        if(req.getGender().isEmpty()) {
            return UserServiceProto.CreateUserResponse.newBuilder()
                    .setResponseCode(400)
                    .setResponseMessage("Gender cant be empty").build();
        }

        UserEntity entity = new UserEntity();
        entity.id = UUID.randomUUID().toString();
        entity.email = req.getEmail();
        entity.firstName = req.getFirstName();
        entity.lastName = req.getLastName();
        entity.gender = req.getGender();
        entity.bio = req.getBio();
        entity.createTime = Instant.now();
        entity.lastUpdateTime = Instant.now();
        userRepository.save(entity);
        User model = new User(entity);
        CompletableFuture.runAsync(() -> {
            log.info("Saving to cache...");
            redisDao.set(REDIS_KEY_PREFIX_USER+entity.id, model);
            log.info("Saved to cache.");
        });
        log.info("Returing asynchrinusly");
        return UserServiceProto.CreateUserResponse.newBuilder()
                .setResponseCode(200)
                .setResponseMessage("User Created")
                .setUser(model.toProto()).build();

    }

    public UserServiceProto.UpdateUserResponse updateUser(UserProto.User req) {
        //validation
        UserEntity entity = null;
        try {
            //not using cache for keeping track for update
            entity = userRepository.findById(req.getId()).orElse(null);
        } catch (Exception e) {
            //pass
        }
        if(entity == null) {
            return UserServiceProto.UpdateUserResponse.newBuilder()
                    .setResponseCode(404)
                    .setResponseMessage("User not found").build();
        }

        if(!req.getEmail().isEmpty())
            entity.email = req.getEmail();
        if(!req.getFirstName().isEmpty())
            entity.firstName = req.getFirstName();
        if(!req.getLastName().isEmpty())
            entity.lastName = req.getLastName();
        if(!req.getGender().isEmpty())
            entity.gender = req.getGender();
        if(!req.getBio().isEmpty())
            entity.bio = req.getBio();

        entity.lastUpdateTime = Instant.now();

        try {
            userRepository.save(entity);
            User model = new User(entity);
            CompletableFuture.runAsync(() -> {
                log.info("Saving to cache...");
                redisDao.set(REDIS_KEY_PREFIX_USER+model.getId(), model);
                log.info("Saved to cache.");
            });
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
                        (new User(entity)).toProto()
                ).build();

    }

}
