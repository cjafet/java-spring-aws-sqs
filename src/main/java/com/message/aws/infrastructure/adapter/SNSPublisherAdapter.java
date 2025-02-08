package com.message.aws.infrastructure.adapter;

import com.message.aws.infrastructure.configuration.SNSConfig;
import com.message.aws.common.exceptions.SNSPublishingException;
import com.message.aws.core.model.domain.VideoMessagePublisher;
import com.message.aws.core.port.SNSProcessorPort;

import com.amazonaws.services.sns.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class SNSPublisherAdapter implements SNSProcessorPort {

    private final SNSConfig snsConfig;
    private final Topic productEventsTopic;
    private final ObjectMapper objectMapper;
    private static final String MESSAGE_GROUP_ID = "meu-message-group-id";


    public SNSPublisherAdapter(@Qualifier("productEventsTopic") Topic productEventsTopic, SNSConfig snsConfig) {
        this.snsConfig = snsConfig;
        this.objectMapper = new ObjectMapper();
        this.productEventsTopic = productEventsTopic;
    }

    @Override
    public void publishMessage(VideoMessagePublisher videoMessage) {
        try {
            Map<String, String> message = new HashMap<>();
            message.put("id", videoMessage.getId().toString());
            message.put("user", videoMessage.getUser());
            message.put("email", videoMessage.getEmail());
            message.put("videoKeyS3", videoMessage.getVideoKeyS3());
            message.put("intervalSeconds", videoMessage.getIntervalSeconds());

            String jsonMessage = objectMapper.writeValueAsString(message);

            PublishRequest publishRequest = new PublishRequest(productEventsTopic.getTopicArn(), jsonMessage)
                    .withMessageGroupId(MESSAGE_GROUP_ID)
                    .withMessageDeduplicationId(UUID.randomUUID().toString());

            PublishResult publishResult = snsConfig.snsClient().publish(publishRequest);

            System.out.println("Mensagem publicada com sucesso! MessageId: " + publishResult.getMessageId());
        } catch (Exception e) {
            throw new SNSPublishingException("Erro ao publicar mensagem no SNS", e);
        }
    }

}
