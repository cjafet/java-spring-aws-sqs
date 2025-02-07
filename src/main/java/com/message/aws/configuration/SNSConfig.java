package com.message.aws.configuration;

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
import software.amazon.awssdk.regions.Region;

public class SNSConfig {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKeyId ;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretAccessKey ;

    @Value("${cloud.aws.credentials.token}")
    private String token;

    String regionName = Region.US_EAST_1.toString();

    @Value("${cloud.aws.credentials.account-id}")
    String accountId;

    @Value("${sns.topic}")
    String topic;

    BasicSessionCredentials credentials = new BasicSessionCredentials(accessKeyId, secretAccessKey, token);

    String arn = "arn:aws:sns:" + regionName +":"+accountId+":"+topic;

    @Bean
    public AmazonSNS snsClient() {
        AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
                .withRegion(regionName)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        SetTopicAttributesRequest setTopicAttributesRequest = new SetTopicAttributesRequest()
                .withTopicArn("arn:aws:sns:" + regionName +":"+accountId+":video-update-status.fifo")
                .withAttributeName("ContentBasedDeduplication")
                .withAttributeValue("true");

        snsClient.setTopicAttributes(setTopicAttributesRequest);

        return snsClient;
    }

    @Bean
    public String arnTopic() {
        return arn;
    }

    @Bean(name = "productEventsTopic")
    public Topic snsProductEventsTopic() {

        GetTopicAttributesRequest getTopicAttributesRequest = new GetTopicAttributesRequest()
                .withTopicArn(arn);
        GetTopicAttributesResult getTopicAttributesResult = snsClient().getTopicAttributes(getTopicAttributesRequest);
        String topicArn = getTopicAttributesResult.getAttributes().get("TopicArn");

        return new Topic().withTopicArn(topicArn);
    }
}
