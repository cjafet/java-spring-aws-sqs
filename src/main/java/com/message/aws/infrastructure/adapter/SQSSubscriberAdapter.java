package com.message.aws.infrastructure.adapter;

import com.message.aws.core.model.domain.VideoMessageSubscriber;
import com.message.aws.core.model.dto.StatusDTO;
import com.message.aws.core.model.dto.UserDTO;
import com.message.aws.core.model.enums.VideoStatus;
import com.message.aws.core.port.SQSSubscriberPort;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Instant;
import org.json.JSONObject;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class SQSSubscriberAdapter implements SQSSubscriberPort {

    private DatabaseAdapter databaseAdapter;

    public SQSSubscriberAdapter(DatabaseAdapter databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
    }

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

            updateVideoStatus(videoMessagePublisher);
            log.info("VideoMessage: {}", videoMessagePublisher);


        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }

    }

    private void updateVideoStatus(VideoMessageSubscriber videoMessagePublisher) {
        Optional<UserDTO> userDTO = databaseAdapter.getUserByEmail(videoMessagePublisher.getEmail());
        Long userId = userDTO.get().getId();
        Optional<StatusDTO> statusId = databaseAdapter.getStatusByVideoId(Long.valueOf(videoMessagePublisher.getId()));

        StatusDTO statusDTO = new StatusDTO(statusId.get().getId(),
                VideoStatus.valueOf(videoMessagePublisher.getStatus()),
                videoMessagePublisher.getVideoKeyS3(),
                null,
                Instant.now().toString(),
                userId,
                Long.valueOf(videoMessagePublisher.getId()));

        databaseAdapter.saveOrUpdateVideoStatus(statusDTO);
    }
}
