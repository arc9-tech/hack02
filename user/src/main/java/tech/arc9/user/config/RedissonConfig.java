package tech.arc9.user.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
public class RedissonConfig {
    Logger log = LoggerFactory.getLogger(RedissonConfig.class);
    @Autowired private UserConfig config;
    @Resource
    private Environment env;

    @Bean
    public RedissonClient getRedisClient(){
        String host = env.getRequiredProperty(config.getRedisCacheHostnameEnvKey());
        Integer port = Integer.parseInt(System.getenv(config.getRedisPortEnvKey()));
        log.info("Redisson client bean host {} port {}", host, port);
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setRetryAttempts(2)
                .setRetryInterval(2000)
                .setConnectionPoolSize(20)
                .setConnectionMinimumIdleSize(10)
                .setDnsMonitoringInterval(5000)
                .setIdleConnectionTimeout(50000);

        return Redisson.create(config);
    }
}
