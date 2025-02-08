package com.message.aws.core.model.entity;

import com.message.aws.core.model.enums.VideoStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StatusTest {

    @Test
    void testStatus_AllFields() {
        Status status = new Status();
        status.setId(1L);
        status.setStatus(VideoStatus.COMPLETED);
        status.setVideoUrl("https://example.com/video.mp4");
        status.setCreatedDate("2024-02-07");
        status.setModifiedDate("2024-02-08");
        status.setUserId(123L);

        assertThat(status.getId()).isEqualTo(1L);
        assertThat(status.getStatus()).isEqualTo(VideoStatus.COMPLETED);
        assertThat(status.getVideoUrl()).isEqualTo("https://example.com/video.mp4");
        assertThat(status.getCreatedDate()).isEqualTo("2024-02-07");
        assertThat(status.getModifiedDate()).isEqualTo("2024-02-08");
        assertThat(status.getUserId()).isEqualTo(123L);
    }

    @Test
    void testStatus_NoArgsConstructor() {
        Status status = new Status();

        assertThat(status.getId()).isNull();
        assertThat(status.getStatus()).isNull();
        assertThat(status.getVideoUrl()).isNull();
        assertThat(status.getCreatedDate()).isNull();
        assertThat(status.getModifiedDate()).isNull();
        assertThat(status.getUserId()).isNull();
    }

    @Test
    void testStatus_SettersAndGetters() {
        Status status = new Status();
        status.setId(10L);
        status.setStatus(VideoStatus.IN_PROGRESS);
        status.setVideoUrl("https://example.com/sample.mp4");
        status.setCreatedDate("2024-02-06");
        status.setModifiedDate("2024-02-09");
        status.setUserId(456L);

        assertThat(status.getId()).isEqualTo(10L);
        assertThat(status.getStatus()).isEqualTo(VideoStatus.IN_PROGRESS);
        assertThat(status.getVideoUrl()).isEqualTo("https://example.com/sample.mp4");
        assertThat(status.getCreatedDate()).isEqualTo("2024-02-06");
        assertThat(status.getModifiedDate()).isEqualTo("2024-02-09");
        assertThat(status.getUserId()).isEqualTo(456L);
    }

    @Test
    void testStatus_EqualityAndHashCode() {
        Status status1 = new Status();
        status1.setId(1L);
        status1.setStatus(VideoStatus.PROCESSING_ERROR);
        status1.setVideoUrl("https://example.com/video.mp4");
        status1.setCreatedDate("2024-02-07");
        status1.setModifiedDate("2024-02-08");
        status1.setUserId(789L);

        Status status2 = new Status();
        status2.setId(1L);
        status2.setStatus(VideoStatus.PROCESSING_ERROR);
        status2.setVideoUrl("https://example.com/video.mp4");
        status2.setCreatedDate("2024-02-07");
        status2.setModifiedDate("2024-02-08");
        status2.setUserId(789L);

        assertThat(status1).isEqualTo(status2);
        assertThat(status1.hashCode()).isEqualTo(status2.hashCode());
    }
}
