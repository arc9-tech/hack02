package tech.arc9.gateway.client;

import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.arc9.gateway.configuration.GatewayConfig;
import tech.arc9.gateway.model.SignedUrl;
import tech.arc9.mediaservice.MediaServiceGrpc;
import tech.arc9.mediaservice.MediaServiceProto;

import javax.annotation.PostConstruct;

@Component
public class MediaServiceClient {

    @Autowired
    private GatewayConfig config;
    private MediaServiceGrpc.MediaServiceBlockingStub mediaServiceBlockingStub;

    @PostConstruct
    private void initialize() {
        mediaServiceBlockingStub = MediaServiceGrpc.newBlockingStub(
                ManagedChannelBuilder.forAddress(
                        config.getMediaServiceHost(),
                        config.getMediaServicePort()
                ).usePlaintext().build()
        );
    }

    public SignedUrl getDpUploadUrl(String userId) {
        return new SignedUrl(
                mediaServiceBlockingStub.getDpUploadUrl(
                        MediaServiceProto.Request.newBuilder().setId(userId).build()
                )
        );
    }

    public SignedUrl getDpDownloadUrl(String userId) {
        return new SignedUrl(
                mediaServiceBlockingStub.getDpDownloadUrl(
                        MediaServiceProto.Request.newBuilder().setId(userId).build()
                )
        );
    }
}
