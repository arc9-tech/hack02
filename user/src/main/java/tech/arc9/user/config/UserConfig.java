package tech.arc9.user.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    private final String redisCacheHostnameEnvKey = "REDIS_CACHE_HOSTNAME";
    private final String redisPortEnvKey = "REDIS_CACHE_PORT";

    private final int port;

    public UserConfig() {
        port = 7991;
    }

    public int getPort() {
        return port;
    }

    public String getRedisCacheHostnameEnvKey() {
        return redisCacheHostnameEnvKey;
    }

    public String getRedisPortEnvKey() {
        return redisPortEnvKey;
    }
}
