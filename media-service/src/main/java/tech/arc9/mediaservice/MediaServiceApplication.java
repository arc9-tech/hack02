package tech.arc9.mediaservice;

import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tech.arc9.mediaservice.config.MediaConfig;
import tech.arc9.mediaservice.server.MediaServiceImpl;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class MediaServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(MediaServiceApplication.class);
    private Server server;

    @Autowired
    private MediaServiceImpl mediaService;
    @Autowired private MediaConfig config;

    @PostConstruct
    public void run() throws IOException, InterruptedException {
        start();
        blockUntilShutdown();
    }

    private void start() throws IOException {
        server = NettyServerBuilder.forPort(config.getPort())
                .addService(mediaService)
                .build()
                .start();
        log.info("Server started, listening on: " + config.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(MediaServiceApplication.this::stop));
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
        SpringApplication.run(MediaServiceApplication.class, args);
    }

}
