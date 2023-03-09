package tech.arc9.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication {
    static Logger log = LoggerFactory.getLogger(GatewayApplication.class);
    public static void main(String[] args) {
//        String sslKeystorePassword = System.getenv("SSL_KEY_STORE_PASSWORD");
//        System.setProperty("server.ssl.key-store-password", sslKeystorePassword);
        SpringApplication.run(GatewayApplication.class, args);
    }

}
