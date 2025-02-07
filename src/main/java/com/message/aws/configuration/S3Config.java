package com.message.aws.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Autowired
    private Environment environment;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKeyId ;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretAccessKey ;

    @Value("${cloud.aws.credentials.token}")
    private String token;

    String regionName = Region.US_EAST_1.toString();


    @Bean
    public S3Client getS3Client() {
        return S3Client
                .builder()
                .region(Region.of(regionName))
                .credentialsProvider(StaticCredentialsProvider.create(AwsSessionCredentials.create(accessKeyId, secretAccessKey,token)))
                .build();
    }


}
