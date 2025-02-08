package com.message.aws.core.model.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class VideoMessagePublisherTest {

    @Test
    void testVideoMessagePublisher_AllFields() {
        VideoMessagePublisher publisher = new VideoMessagePublisher();
        publisher.setId("123");
        publisher.setUser("JohnDoe");
        publisher.setEmail("john.doe@example.com");
        publisher.setVideoKeyS3("videos/sample-video.mp4");
        publisher.setIntervalSeconds("60");

        assertThat(publisher.getId()).isEqualTo("123");
        assertThat(publisher.getUser()).isEqualTo("JohnDoe");
        assertThat(publisher.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(publisher.getVideoKeyS3()).isEqualTo("videos/sample-video.mp4");
        assertThat(publisher.getIntervalSeconds()).isEqualTo("60");
    }

    @Test
    void testVideoMessagePublisher_NoArgsConstructor() {
        VideoMessagePublisher publisher = new VideoMessagePublisher();

        assertThat(publisher.getId()).isNull();
        assertThat(publisher.getUser()).isNull();
        assertThat(publisher.getEmail()).isNull();
        assertThat(publisher.getVideoKeyS3()).isNull();
        assertThat(publisher.getIntervalSeconds()).isNull();
    }

    @Test
    void testVideoMessagePublisher_SettersAndGetters() {
        VideoMessagePublisher publisher = new VideoMessagePublisher();
        publisher.setId("999");
        publisher.setUser("JaneDoe");
        publisher.setEmail("jane.doe@example.com");
        publisher.setVideoKeyS3("videos/test-video.mp4");
        publisher.setIntervalSeconds("30");

        assertThat(publisher.getId()).isEqualTo("999");
        assertThat(publisher.getUser()).isEqualTo("JaneDoe");
        assertThat(publisher.getEmail()).isEqualTo("jane.doe@example.com");
        assertThat(publisher.getVideoKeyS3()).isEqualTo("videos/test-video.mp4");
        assertThat(publisher.getIntervalSeconds()).isEqualTo("30");
    }

    @Test
    void testVideoMessagePublisher_EqualityAndHashCode() {
        VideoMessagePublisher publisher1 = new VideoMessagePublisher();
        publisher1.setId("456");
        publisher1.setUser("Alice");
        publisher1.setEmail("alice@example.com");
        publisher1.setVideoKeyS3("videos/alice-video.mp4");
        publisher1.setIntervalSeconds("45");

        VideoMessagePublisher publisher2 = new VideoMessagePublisher();
        publisher2.setId("456");
        publisher2.setUser("Alice");
        publisher2.setEmail("alice@example.com");
        publisher2.setVideoKeyS3("videos/alice-video.mp4");
        publisher2.setIntervalSeconds("45");

        assertThat(publisher1).usingRecursiveComparison().isEqualTo(publisher2);
    }
}
