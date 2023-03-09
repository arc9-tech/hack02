package tech.arc9.gateway.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.arc9.gateway.client.UserServiceClient;
import tech.arc9.gateway.model.ErrorResponse;
import tech.arc9.gateway.model.User;
import tech.arc9.gateway.model.UserCreateModel;
import tech.arc9.user.UserServiceProto;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import javax.validation.Valid;

@RestController
public class GatewayController {

    @Autowired private UserServiceClient userServiceClient;


    @GetMapping("/hello")
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

    //POST user --> create user {name, email }
    //PATCH user --> update name
    //GET user --> user list
    //GET user/{userId} --> user details

    @GetMapping("/user/{userId}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = User.class, message = "User found"),
            @ApiResponse(code = 404, response = ErrorResponse.class, message = "Not found")
    })
    public ResponseEntity getUserDetails(HttpServletRequest request,
                                         @RequestHeader("Authorization") String authorization,
                                         @PathVariable String userId) {
        UserServiceProto.GetUserDetailsResponse response = userServiceClient.getUserDetails(userId);

        if(response.getResponseCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(new User(
                    response.getUser().getName(),
                    response.getUser().getEmail(),
                    response.getUser().getId()

            ));
        }
        return ResponseEntity.status(response.getResponseCode()).body(
                new ErrorResponse(
                       HttpStatus.resolve(response.getResponseCode()),
                       response.getResponseMessage()
                )
        );
    }

    @PostMapping("/user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = User.class, message = "User found"),
            @ApiResponse(code = 404, response = ErrorResponse.class, message = "Not found")
    })
    public ResponseEntity createUser(
            HttpServletRequest request,
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody UserCreateModel userCreateModel
            ) {
        return null;
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
