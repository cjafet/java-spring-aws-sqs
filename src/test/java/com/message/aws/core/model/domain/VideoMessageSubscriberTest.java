package com.message.aws.core.model.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class VideoMessageSubscriberTest {

    @Test
    void testVideoMessageSubscriber_AllFields() {
        VideoMessageSubscriber subscriber = new VideoMessageSubscriber();
        subscriber.setId("123");
        subscriber.setUser("JohnDoe");
        subscriber.setEmail("john.doe@example.com");
        subscriber.setVideoKeyS3("videos/sample-video.mp4");
        subscriber.setZipKeyS3("archives/sample.zip");
        subscriber.setVideoUrlS3("https://s3.amazonaws.com/videos/sample.mp4");
        subscriber.setStatus("COMPLETED");

        assertThat(subscriber.getId()).isEqualTo("123");
        assertThat(subscriber.getUser()).isEqualTo("JohnDoe");
        assertThat(subscriber.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(subscriber.getVideoKeyS3()).isEqualTo("videos/sample-video.mp4");
        assertThat(subscriber.getZipKeyS3()).isEqualTo("archives/sample.zip");
        assertThat(subscriber.getVideoUrlS3()).isEqualTo("https://s3.amazonaws.com/videos/sample.mp4");
        assertThat(subscriber.getStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void testVideoMessageSubscriber_NoArgsConstructor() {
        VideoMessageSubscriber subscriber = new VideoMessageSubscriber();

        assertThat(subscriber.getId()).isNull();
        assertThat(subscriber.getUser()).isNull();
        assertThat(subscriber.getEmail()).isNull();
        assertThat(subscriber.getVideoKeyS3()).isNull();
        assertThat(subscriber.getZipKeyS3()).isNull();
        assertThat(subscriber.getVideoUrlS3()).isNull();
        assertThat(subscriber.getStatus()).isNull();
    }

    @Test
    void testVideoMessageSubscriber_SettersAndGetters() {
        VideoMessageSubscriber subscriber = new VideoMessageSubscriber();
        subscriber.setId("999");
        subscriber.setUser("JaneDoe");
        subscriber.setEmail("jane.doe@example.com");
        subscriber.setVideoKeyS3("videos/test-video.mp4");
        subscriber.setZipKeyS3("archives/test.zip");
        subscriber.setVideoUrlS3("https://s3.amazonaws.com/videos/test.mp4");
        subscriber.setStatus("PROCESSING");

        assertThat(subscriber.getId()).isEqualTo("999");
        assertThat(subscriber.getUser()).isEqualTo("JaneDoe");
        assertThat(subscriber.getEmail()).isEqualTo("jane.doe@example.com");
        assertThat(subscriber.getVideoKeyS3()).isEqualTo("videos/test-video.mp4");
        assertThat(subscriber.getZipKeyS3()).isEqualTo("archives/test.zip");
        assertThat(subscriber.getVideoUrlS3()).isEqualTo("https://s3.amazonaws.com/videos/test.mp4");
        assertThat(subscriber.getStatus()).isEqualTo("PROCESSING");
    }

    @Test
    void testVideoMessageSubscriber_EqualityAndHashCode() {
        VideoMessageSubscriber subscriber1 = new VideoMessageSubscriber();
        subscriber1.setId("456");
        subscriber1.setUser("Alice");
        subscriber1.setEmail("alice@example.com");
        subscriber1.setVideoKeyS3("videos/alice-video.mp4");
        subscriber1.setZipKeyS3("archives/alice.zip");
        subscriber1.setVideoUrlS3("https://s3.amazonaws.com/videos/alice.mp4");
        subscriber1.setStatus("FAILED");

        VideoMessageSubscriber subscriber2 = new VideoMessageSubscriber();
        subscriber2.setId("456");
        subscriber2.setUser("Alice");
        subscriber2.setEmail("alice@example.com");
        subscriber2.setVideoKeyS3("videos/alice-video.mp4");
        subscriber2.setZipKeyS3("archives/alice.zip");
        subscriber2.setVideoUrlS3("https://s3.amazonaws.com/videos/alice.mp4");
        subscriber2.setStatus("FAILED");

        assertThat(subscriber1).usingRecursiveComparison().isEqualTo(subscriber2);
    }
}
