package tech.arc9.user;

import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tech.arc9.user.config.UserConfig;
import tech.arc9.user.server.UserServiceImpl;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class UserApplication {
    private static final Logger log = LoggerFactory.getLogger(UserApplication.class);
//    private int port;
    private Server server;

//    private BindableService userService;
    @Autowired private UserServiceImpl userService;
    @Autowired private UserConfig config;


    @PostConstruct
    public void run() throws IOException, InterruptedException {
        start();
        blockUntilShutdown();
    }

    private void start() throws IOException {
        server = NettyServerBuilder.forPort(config.getPort())
                .addService(userService)
                .build()
                .start();
        log.info("Server started, listening on: " + config.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(UserApplication.this::stop));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
