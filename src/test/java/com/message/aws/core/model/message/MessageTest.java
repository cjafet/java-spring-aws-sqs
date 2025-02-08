package com.message.aws.core.model.message;

import com.message.aws.core.model.enums.VideoStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void testMessage_AllFields() {
        Message message = Message.builder()
                .user("johndoe")
                .videoUrl("https://example.com/video.mp4")
                .createdDate("2024-02-07")
                .modifiedDate("2024-02-08")
                .status(VideoStatus.COMPLETED)
                .build();

        assertEquals("johndoe", message.getUser());
        assertEquals("https://example.com/video.mp4", message.getVideoUrl());
        assertEquals("2024-02-07", message.getCreatedDate());
        assertEquals("2024-02-08", message.getModifiedDate());
        assertEquals(VideoStatus.COMPLETED, message.getStatus());
    }

    @Test
    void testMessage_NoArgsConstructor() {
        Message message = new Message();

        assertNull(message.getUser());
        assertNull(message.getVideoUrl());
        assertNull(message.getCreatedDate());
        assertNull(message.getModifiedDate());
        assertNull(message.getStatus());
    }

    @Test
    void testMessage_SettersAndGetters() {
        Message message = new Message();
        message.setUser("janedoe");
        message.setVideoUrl("https://example.com/sample.mp4");
        message.setCreatedDate("2024-02-06");
        message.setModifiedDate("2024-02-09");
        message.setStatus(VideoStatus.IN_PROGRESS);

        assertEquals("janedoe", message.getUser());
        assertEquals("https://example.com/sample.mp4", message.getVideoUrl());
        assertEquals("2024-02-06", message.getCreatedDate());
        assertEquals("2024-02-09", message.getModifiedDate());
        assertEquals(VideoStatus.IN_PROGRESS, message.getStatus());
    }

    @Test
    void testMessage_EqualityAndHashCode() {
        Message message1 = Message.builder()
                .user("user1")
                .videoUrl("https://example.com/video1.mp4")
                .createdDate("2024-02-07")
                .modifiedDate("2024-02-08")
                .status(VideoStatus.PROCESSING_ERROR)
                .build();

        Message message2 = Message.builder()
                .user("user1")
                .videoUrl("https://example.com/video1.mp4")
                .createdDate("2024-02-07")
                .modifiedDate("2024-02-08")
                .status(VideoStatus.PROCESSING_ERROR)
                .build();

        assertEquals(message1, message2);
        assertEquals(message1.hashCode(), message2.hashCode());
    }
}
