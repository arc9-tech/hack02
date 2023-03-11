package tech.arc9.mediaservice.manager;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import tech.arc9.mediaservice.MediaServiceProto;
import tech.arc9.mediaservice.config.MediaConfig;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class MediaManager {
    Logger log = LoggerFactory.getLogger(MediaManager.class);
    @Resource private Environment env;
    @Autowired private MediaConfig config;
    @Autowired private AmazonS3 s3Client;
    private static final int urlExpiryInSeconds = 30 * 60;
    private String s3BucketName;

    @PostConstruct
    private void init() {
        this.s3BucketName = env.getRequiredProperty(config.getS3bucketEnvKey());

//        test();
    }


    private void test() {

        log.info("Hello");

        String objectKey = "example-object-key.txt";
        Date expiration = new Date(System.currentTimeMillis() + 3600000); // 1 hour from now
        GeneratePresignedUrlRequest uploadRequest = new GeneratePresignedUrlRequest(s3BucketName, objectKey)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);
        URL uploadUrl = s3Client.generatePresignedUrl(uploadRequest);
        log.info("Pre-signed URL for uploading: {}", uploadUrl);

        GeneratePresignedUrlRequest downloadRequest = new GeneratePresignedUrlRequest(s3BucketName, objectKey)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        URL downloadUrl = s3Client.generatePresignedUrl(downloadRequest);
        log.info("Pre-signed URL for downloading: {}", downloadUrl);


    }


    public MediaServiceProto.Response getDpUploadUrl(MediaServiceProto.Request request) {
        Date expiration = new Date(System.currentTimeMillis() + (urlExpiryInSeconds * 1000)); // expiration in milliseconds
        String objectKey = request.getId();

        GeneratePresignedUrlRequest uploadRequest = new GeneratePresignedUrlRequest(s3BucketName, objectKey)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);
        URL uploadUrl = s3Client.generatePresignedUrl(uploadRequest);
        String url = uploadUrl.toString();
        log.info("getDpUploadUrl {} {} {}", objectKey, expiration, uploadUrl);
        log.info(url);

        return MediaServiceProto.Response.newBuilder().setSasUrl(url).setExpiredAt(expiration.getTime()).build();
    }

    public MediaServiceProto.Response getDpDownloadUrl(MediaServiceProto.Request request) {
        Date expiration = new Date(System.currentTimeMillis() + (urlExpiryInSeconds * 1000)); // expiration in milliseconds
        String objectKey = request.getId();

        GeneratePresignedUrlRequest downloadRequest = new GeneratePresignedUrlRequest(s3BucketName, objectKey)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        URL downloadUrl = s3Client.generatePresignedUrl(downloadRequest);
        String url = downloadUrl.toString();

        return MediaServiceProto.Response.newBuilder().setSasUrl(url).setExpiredAt(expiration.getTime()).build();
    }
}
