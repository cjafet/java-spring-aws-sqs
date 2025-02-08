package com.message.aws.port;

import org.springframework.messaging.Message;

public interface SQSSubscriberPort {
    public void receiveMessage(Message<String> message);
}
