package com.message.aws.infrastructure.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.GetTopicAttributesRequest;
import com.amazonaws.services.sns.model.GetTopicAttributesResult;
import com.amazonaws.services.sns.model.SetTopicAttributesRequest;
import com.amazonaws.services.sns.model.Topic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SNSConfig {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKeyId ;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretAccessKey ;

    @Value("${cloud.aws.credentials.token}")
    private String token;

    String regionName = "us-east-1";

    @Value("${cloud.aws.credentials.account-id}")
    String accountId;

    String topicArn = "arn:aws:sns:us-east-1:090111931170:video-status-topic.fifo";

    @Bean
    public AmazonSNS snsClient() {
        BasicSessionCredentials credentials = new BasicSessionCredentials(accessKeyId, secretAccessKey, token);

        AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
                .withRegion(regionName)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        SetTopicAttributesRequest setTopicAttributesRequest = new SetTopicAttributesRequest()
                .withTopicArn(topicArn)
                .withAttributeName("ContentBasedDeduplication")
                .withAttributeValue("true");

        snsClient.setTopicAttributes(setTopicAttributesRequest);

        return snsClient;
    }

    @Bean(name = "productEventsTopic")
    public Topic snsProductEventsTopic() {

        GetTopicAttributesRequest getTopicAttributesRequest = new GetTopicAttributesRequest()
                .withTopicArn(topicArn);
        GetTopicAttributesResult getTopicAttributesResult = snsClient().getTopicAttributes(getTopicAttributesRequest);

        topicArn = getTopicAttributesResult.getAttributes().get("TopicArn");


        return new Topic().withTopicArn(topicArn);
    }}
