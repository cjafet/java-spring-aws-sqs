package com.message.aws.core.model.entity;

import com.message.aws.core.model.enums.VideoStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StatusEntityTest {

    @Test
    void testStatus_AllFields() {
        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setId(1L);
        statusEntity.setStatus(VideoStatus.COMPLETED);
        statusEntity.setVideoKey("https://example.com/video.mp4");
        statusEntity.setCreatedDate("2024-02-07");
        statusEntity.setModifiedDate("2024-02-08");
        statusEntity.setUserId(123L);

        assertThat(statusEntity.getId()).isEqualTo(1L);
        assertThat(statusEntity.getStatus()).isEqualTo(VideoStatus.COMPLETED);
        assertThat(statusEntity.getVideoKey()).isEqualTo("https://example.com/video.mp4");
        assertThat(statusEntity.getCreatedDate()).isEqualTo("2024-02-07");
        assertThat(statusEntity.getModifiedDate()).isEqualTo("2024-02-08");
        assertThat(statusEntity.getUserId()).isEqualTo(123L);
    }

    @Test
    void testStatus_NoArgsConstructor() {
        StatusEntity statusEntity = new StatusEntity();

        assertThat(statusEntity.getId()).isNull();
        assertThat(statusEntity.getStatus()).isNull();
        assertThat(statusEntity.getVideoKey()).isNull();
        assertThat(statusEntity.getCreatedDate()).isNull();
        assertThat(statusEntity.getModifiedDate()).isNull();
        assertThat(statusEntity.getUserId()).isNull();
    }

    @Test
    void testStatus_SettersAndGetters() {
        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setId(10L);
        statusEntity.setStatus(VideoStatus.IN_PROGRESS);
        statusEntity.setVideoKey("https://example.com/sample.mp4");
        statusEntity.setCreatedDate("2024-02-06");
        statusEntity.setModifiedDate("2024-02-09");
        statusEntity.setUserId(456L);

        assertThat(statusEntity.getId()).isEqualTo(10L);
        assertThat(statusEntity.getStatus()).isEqualTo(VideoStatus.IN_PROGRESS);
        assertThat(statusEntity.getVideoKey()).isEqualTo("https://example.com/sample.mp4");
        assertThat(statusEntity.getCreatedDate()).isEqualTo("2024-02-06");
        assertThat(statusEntity.getModifiedDate()).isEqualTo("2024-02-09");
        assertThat(statusEntity.getUserId()).isEqualTo(456L);
    }

    @Test
    void testStatus_EqualityAndHashCode() {
        StatusEntity statusEntity1 = new StatusEntity();
        statusEntity1.setId(1L);
        statusEntity1.setStatus(VideoStatus.PROCESSING_ERROR);
        statusEntity1.setVideoKey("https://example.com/video.mp4");
        statusEntity1.setCreatedDate("2024-02-07");
        statusEntity1.setModifiedDate("2024-02-08");
        statusEntity1.setUserId(789L);

        StatusEntity statusEntity2 = new StatusEntity();
        statusEntity2.setId(1L);
        statusEntity2.setStatus(VideoStatus.PROCESSING_ERROR);
        statusEntity2.setVideoKey("https://example.com/video.mp4");
        statusEntity2.setCreatedDate("2024-02-07");
        statusEntity2.setModifiedDate("2024-02-08");
        statusEntity2.setUserId(789L);

        assertThat(statusEntity1).isEqualTo(statusEntity2);
        assertThat(statusEntity1.hashCode()).isEqualTo(statusEntity2.hashCode());
    }
}
