package tech.arc9.mediaservice.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
public class S3Config {
    Logger log = LoggerFactory.getLogger(S3Config.class);

    @Autowired private MediaConfig config;
    @Resource
    private Environment env;
    @Bean
    public AmazonS3 getAmazonS3() {

        String endpointUrl = env.getRequiredProperty(config.getS3EndpointEnvKey());
        String region = env.getRequiredProperty(config.getS3RegionEnvKey());
        String accessKey = env.getRequiredProperty(config.getS3AccessKeyEnvKey());
        String secretKey = env.getRequiredProperty(config.getS3SecretKeyEnvKey());
        String bucketName = env.getRequiredProperty(config.getS3bucketEnvKey());
        log.info("S3Config endpoint {} region {} accessKey {} secretKey {} bucketName {}", endpointUrl, region, accessKey, secretKey, bucketName);

        log.info("s3Config url {}", endpointUrl);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpointUrl, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
        return s3Client;
    }
}
