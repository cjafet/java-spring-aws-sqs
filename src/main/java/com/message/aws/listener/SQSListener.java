package com.message.aws.listener;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SQSListener {

    @Value("${sqs.queue-name-consumer}")
    private String queueName;

    private final AmazonSQS amazonSQSClient;
    private final AmazonSQSAsync amazonSQSAsync;


    public SQSListener(AmazonSQS amazonSQSClient, AmazonSQSAsync amazonSQSAsync) {
        this.amazonSQSClient = amazonSQSClient;
        this.amazonSQSAsync = amazonSQSAsync;
    }

    @SqsListener(value = "StatusQueue")
    public void onStatusEvent(String rawMessage) {
        log.info("Incoming EventStatusNoticiation: {}", rawMessage);

    }

    // Runs every 5 seconds.
    @Scheduled(fixedDelay = 5000)
    public void consumeMessages() {
        try {
            String queueUrl = amazonSQSClient.getQueueUrl(this.queueName).getQueueUrl();
            ReceiveMessageResult receiveMessageResult = amazonSQSClient.receiveMessage(queueUrl);

            if (!receiveMessageResult.getMessages().isEmpty()) {
                Message message = receiveMessageResult.getMessages().get(0);
                log.info("Read Message from queue: {}", message.getBody());
                amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
            }

        } catch (Exception e) {
            log.error("Queue Exception Message: {}", e.getMessage());
        }
    }
}
