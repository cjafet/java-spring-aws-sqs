package com.message.aws.port;


import com.message.aws.model.domain.VideoMessagePublisher;

public interface SNSProcessorPort {
    void publishMessage(VideoMessagePublisher videoMessagePublisher);
}
