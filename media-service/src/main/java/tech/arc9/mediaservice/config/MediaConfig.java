package tech.arc9.mediaservice.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MediaConfig {


    private final int port;

    private final String s3EndpointEnvKey = "S3_ENDPOINT";
    private final String s3RegionEnvKey = "S3_REGION";
    private final String s3AccessKeyEnvKey = "S3_ACCESS_KEY";
    private final String s3SecretKeyEnvKey = "S3_SECRET_KEY";

    private final String s3bucketEnvKey = "S3_BUCKET";

    public MediaConfig() {
        port = 7992;
    }

    public int getPort() {
        return port;
    }

    public String getS3EndpointEnvKey() {
        return s3EndpointEnvKey;
    }

    public String getS3RegionEnvKey() {
        return s3RegionEnvKey;
    }

    public String getS3AccessKeyEnvKey() {
        return s3AccessKeyEnvKey;
    }

    public String getS3SecretKeyEnvKey() {
        return s3SecretKeyEnvKey;
    }

    public String getS3bucketEnvKey() {
        return s3bucketEnvKey;
    }
}
