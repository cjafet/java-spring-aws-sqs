package com.message.aws.configuration;

import com.amazonaws.auth.*;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SQSConfig {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKeyId ;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretAccessKey ;

    @Value("${cloud.aws.credentials.token}")
    private String token;

    @Bean
    public AmazonSQS amazonSQSClient() {

        BasicSessionCredentials awsCredentials = new BasicSessionCredentials(accessKeyId, secretAccessKey, token);
        return AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    @Bean
    public AmazonSQSAsync sqsClient() {
        AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration(
                "sqs.us-east-1.amazonaws.com",
                "us-east-1"
        );

        BasicSessionCredentials awsCredentials = new BasicSessionCredentials(accessKeyId, secretAccessKey, token);

        return AmazonSQSAsyncClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfig)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
