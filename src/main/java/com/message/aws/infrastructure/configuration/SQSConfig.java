package com.message.aws.infrastructure.configuration;

import com.amazonaws.auth.*;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import io.awspring.cloud.autoconfigure.sqs.SqsProperties;
import io.awspring.cloud.sqs.listener.QueueNotFoundStrategy;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

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


    @Bean
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient) {
        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .configure(o -> o.queueNotFoundStrategy(QueueNotFoundStrategy.FAIL))
                .build();
    }

    @Bean
    SqsAsyncClient sqsAsyncClient(SqsProperties properties) {
        AwsSessionCredentials credentials = AwsSessionCredentials.create(accessKeyId, secretAccessKey, token);
        String queueUrl = "https://sqs.us-east-1.amazonaws.com/090111931170/video-carregado-subscriber-queue.fifo";
        return SqsAsyncClient.builder()
                .credentialsProvider( StaticCredentialsProvider.create(credentials))
                .region(Region.of("us-east-1"))
                .endpointOverride(URI.create(queueUrl))
                .build();
    }
}
