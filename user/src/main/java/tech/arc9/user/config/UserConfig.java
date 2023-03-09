package tech.arc9.user.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    private final int port;

    public UserConfig() {
        port = 7991;
    }

    public int getPort() {
        return port;
    }

}
