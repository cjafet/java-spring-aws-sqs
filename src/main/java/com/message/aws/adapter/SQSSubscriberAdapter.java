package com.message.aws.adapter;

import com.message.aws.model.domain.VideoMessageSubscriber;
import com.message.aws.port.SQSSubscriberPort;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SQSSubscriberAdapter implements SQSSubscriberPort {

    @Override
    @SqsListener("${sqs.queue-name-consumer}")
    public void receiveMessage(Message<String> message) {
        String content = message.getPayload();
        if (content == null) {
            log.error("Received null content from SQS");
            return;
        }


        try {
            JSONObject snsMessage = new JSONObject(content);

            String messageContent = snsMessage.getString("Message");

            JSONObject videoMessageJson = new JSONObject(messageContent);

            VideoMessageSubscriber videoMessagePublisher = new VideoMessageSubscriber();
            videoMessagePublisher.setZipKeyS3(videoMessageJson.getString("zipKeyS3"));
            videoMessagePublisher.setVideoKeyS3(videoMessageJson.getString("videoKeyS3"));
            videoMessagePublisher.setVideoUrlS3(videoMessageJson.getString("videoUrlS3"));
            videoMessagePublisher.setId(videoMessageJson.getString("id"));
            videoMessagePublisher.setUser(videoMessageJson.getString("user"));
            videoMessagePublisher.setEmail(videoMessageJson.getString("email"));
            videoMessagePublisher.setStatus(videoMessageJson.getString("status"));

            log.info("VideoMessage: {}", videoMessagePublisher);



        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }

    }
}
