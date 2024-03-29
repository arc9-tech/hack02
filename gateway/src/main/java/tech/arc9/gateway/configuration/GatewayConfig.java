package tech.arc9.gateway.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class GatewayConfig {
    Logger log = LoggerFactory.getLogger(GatewayConfig.class);
    private String swaggerUsername;
    private String swaggerPassword;

    private String userServiceHost;
    private int userServicePort;


    private String mediaServiceHost;
    private int mediaServicePort;

    private String apiKey;

    @PostConstruct
    private void initialize() {
        swaggerUsername = System.getenv("API_DOC_USER");
        if(swaggerUsername == null || swaggerUsername.isEmpty()) {
            log.error("API_DOC_USER not set");
            System.exit(-1);
        }

        swaggerPassword = System.getenv("API_DOC_PASSWORD");
        if(swaggerPassword == null || swaggerPassword.isEmpty()) {
            log.error("API_DOC_PASSWORD not set");
            System.exit(-1);
        }

        userServiceHost = System.getenv("USER_SERVICE_HOST");
        if(userServiceHost == null || userServiceHost.isEmpty()) {
            log.error("USER_SERVICE_HOST not set");
            System.exit(-1);
        }

        String userServicePortStr = System.getenv("USER_SERVICE_PORT");
        if(userServicePortStr == null || userServicePortStr.isEmpty()) {
            log.error("USER_SERVICE_PORT not set");
            System.exit(-1);
        }
        try {
            userServicePort = Integer.parseInt(userServicePortStr);
        } catch (Exception ex) {
            log.error("USER_SERVICE_PORT is not a integer");
            System.exit(-1);
        }

        mediaServiceHost = System.getenv("MEDIA_SERVICE_HOST");
        if(mediaServiceHost == null || mediaServiceHost.isEmpty()) {
            log.error("MEDIA_SERVICE_HOST not set");
            System.exit(-1);
        }

        String mediaServicePortStr = System.getenv("MEDIA_SERVICE_PORT");
        if(mediaServicePortStr == null || mediaServicePortStr.isEmpty()) {
            log.error("MEDIA_SERVICE_PORT not set");
            System.exit(-1);
        }
        try {
            mediaServicePort = Integer.parseInt(mediaServicePortStr);
        } catch (Exception ex) {
            log.error("MEDIA_SERVICE_PORT is not a integer");
            System.exit(-1);
        }

        apiKey = System.getenv("API_KEY");
        if(apiKey == null || apiKey.isEmpty()) {
            log.error("API_KEY not set");
            System.exit(-1);
        }

    }

    public String getSwaggerUsername() {
        return swaggerUsername;
    }

    public String getSwaggerPassword() {
        return swaggerPassword;
    }

    public String getUserServiceHost() {
        return userServiceHost;
    }

    public int getUserServicePort() {
        return userServicePort;
    }

    public String getMediaServiceHost() {
        return mediaServiceHost;
    }

    public int getMediaServicePort() {
        return mediaServicePort;
    }

    public String getApiKey() {
        return apiKey;
    }
}
