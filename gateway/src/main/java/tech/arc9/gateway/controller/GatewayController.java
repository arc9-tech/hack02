package tech.arc9.gateway.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.arc9.gateway.client.MediaServiceClient;
import tech.arc9.gateway.client.UserServiceClient;
import tech.arc9.gateway.model.ErrorResponse;
import tech.arc9.gateway.model.SignedUrl;
import tech.arc9.gateway.model.User;
import tech.arc9.gateway.model.UserCreateModel;
import tech.arc9.user.UserProto;
import tech.arc9.user.UserServiceProto;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.validation.Valid;

@RestController
public class GatewayController {
    Logger log = LoggerFactory.getLogger(GatewayController.class);
    @Autowired private UserServiceClient userServiceClient;
    @Autowired private MediaServiceClient mediaServiceClient;


    @GetMapping(value = "/hello", produces = "application/json")
    public JsonNode hello(HttpServletRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("yourIp", request.getRemoteAddr() );

        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                json.put(headerName, request.getHeader(headerName));
            }
        }
        return json;
    }


    @GetMapping(value = "/user/{userId}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = User.class, message = "User found"),
            @ApiResponse(code = 404, response = ErrorResponse.class, message = "Not found")
    })
    public ResponseEntity getUserDetails(HttpServletRequest request,
                                         @RequestHeader("Authorization") String authorization,
                                         @PathVariable String userId) {
        log.info("Request recived for user details {}", userId);

        UserServiceProto.GetUserDetailsResponse response = userServiceClient.getUserDetails(userId);

        if(response.getResponseCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(new User(response.getUser()));
        }
        return ResponseEntity.status(response.getResponseCode()).body(
                new ErrorResponse(
                       HttpStatus.resolve(response.getResponseCode()),
                       response.getResponseMessage()
                )
        );
    }


    @GetMapping(value = "/user", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = User[].class, message = "User found"),
            @ApiResponse(code = 404, response = ErrorResponse.class, message = "Not found")
    })
    public ResponseEntity getUserList(HttpServletRequest request,
                                         @RequestHeader("Authorization") String authorization,
                                        @RequestParam(value="offset", defaultValue="0") int offset,
                                        @RequestParam(value="limit", defaultValue="10") int limit) {
        UserServiceProto.GetUserListResponse response = userServiceClient.getUserList(limit, offset);

        if(response.getResponseCode() != HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getResponseCode()).body(
                    new ErrorResponse(
                            HttpStatus.resolve(response.getResponseCode()),
                            response.getResponseMessage()
                    )
            );
        }
        List<User> userList = new ArrayList<>();
        for(UserProto.User proto : response.getUsersList()) {
            userList.add(new User(proto));
        }
        return ResponseEntity.ok(userList);
    }

    @PostMapping(value = "/user", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = User.class, message = "User found"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Invalid request")
    })
    public ResponseEntity createUser(
            HttpServletRequest request,
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody UserCreateModel userCreateModel
            ) {
        UserServiceProto.CreateUserResponse response =
        userServiceClient.createUser(userCreateModel.getName(), userCreateModel.getEmail());
        if(response.getResponseCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(new User(response.getUser()));
        }
        return ResponseEntity.status(response.getResponseCode()).body(
                new ErrorResponse(
                        HttpStatus.resolve(response.getResponseCode()),
                        response.getResponseMessage()
                )
        );
    }
    @PatchMapping(value = "/user/{userId}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = User.class, message = "User found"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "Invalid request")
    })
    public ResponseEntity updateUser(
            HttpServletRequest request,
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody UserCreateModel userCreateModel,
            @PathVariable String userId
    ) {
        UserServiceProto.UpdateUserResponse response =
                userServiceClient.updateUser(userId, userCreateModel.getName(), userCreateModel.getEmail());
        if(response.getResponseCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(new User(response.getUser()));
        }
        return ResponseEntity.status(response.getResponseCode()).body(
                new ErrorResponse(
                        HttpStatus.resolve(response.getResponseCode()),
                        response.getResponseMessage()
                )
        );
    }



    @GetMapping(value = "/user/{userId}/dp-download-url", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = SignedUrl.class, message = "User found"),
    })
    public ResponseEntity getUserDpDownloadUrl(HttpServletRequest request,
                                         @RequestHeader("Authorization") String authorization,
                                         @PathVariable String userId) {
        log.info("Request recived for user details {}", userId);
        return ResponseEntity.ok(mediaServiceClient.getDpDownloadUrl(userId));
    }

    @GetMapping(value = "/user/{userId}/dp-upload-url", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = SignedUrl.class, message = "User found"),
    })
    public ResponseEntity getUserDpUploadUrl(HttpServletRequest request,
                                               @RequestHeader("Authorization") String authorization,
                                               @PathVariable String userId) {
        log.info("Request recived for user details {}", userId);
        return ResponseEntity.ok(mediaServiceClient.getDpUploadUrl(userId));
    }



    //GET user/{userId}/avatar --> returns signed url
    //grpc 1
    //grpc 2
    //redis
    //docker all 3 app
    //docker mysql
    //docker redis
    //elastic stack , apm, elastic,kibana
}
