package com.message.aws.core.port;


import com.message.aws.core.model.domain.VideoMessagePublisher;

public interface SNSPublisherPort {
    void publishMessage(VideoMessagePublisher videoMessagePublisher);
}
