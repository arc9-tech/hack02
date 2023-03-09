package tech.arc9.gateway.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
public class GatewayController {

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
    public JsonNode getUserDetails(HttpServletRequest request, @PathVariable String userId) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("id", userId);
        json.put("name", "Tariqul Islam");
        json.put("email", "tarik.amtoly@gmail.com");
        return json;
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
