package com.message.aws.core.model.entity;

import com.message.aws.core.model.dto.UserVideosDTO;
import com.message.aws.core.model.enums.VideoStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoEntityTest {

//    @Test
//    void testVideoEntity_AllFields() {
//        VideoEntity videoEntity = new VideoEntity();
//        videoEntity.setId(1L);
//        videoEntity.setVideoStatus(VideoStatus.IN_PROGRESS);
//        videoEntity.setVideoKey("https://example.com/video.mp4");
//        videoEntity.setVideoKey("video.mp4");
//        videoEntity.setUserId(100L);
//
//        assertEquals(1L, videoEntity.getId());
//        assertEquals(VideoStatus.IN_PROGRESS, videoEntity.getVideoStatus());
//        assertEquals("https://example.com/video.mp4", videoEntity.getVideoKey());
//        assertEquals("video.mp4", videoEntity.getVideoKey());
//        assertEquals(100L, videoEntity.getUserId());
//    }
//
//    @Test
//    void testUserVideosDTO_AllFields() {
//        UserVideosDTO userVideosDTO = new UserVideosDTO(2L, VideoStatus.COMPLETED, "processed.mp4", 200L);
//
//        assertEquals(2L, userVideosDTO.getId());
//        assertEquals(VideoStatus.COMPLETED, userVideosDTO.getVideoStatus());
//        assertEquals("https://example.com/processed.mp4", userVideosDTO.getVideoKey());
//        assertEquals("processed.mp4", userVideosDTO.getVideoKey());
//        assertEquals(200L, userVideosDTO.getUserId());
//    }

    @Test
    void testVideoEntity_EqualityAndHashCode() {
        VideoEntity video1 = new VideoEntity();
        video1.setId(3L);
        video1.setVideoStatus(VideoStatus.PROCESSING_ERROR);
        video1.setVideoKey("https://example.com/error.mp4");
        video1.setVideoKey("video.mp4");
        video1.setUserId(300L);

        VideoEntity video2 = new VideoEntity();
        video2.setId(3L);
        video2.setVideoStatus(VideoStatus.PROCESSING_ERROR);
        video2.setVideoKey("https://example.com/error.mp4");
        video2.setVideoKey("video.mp4");
        video2.setUserId(300L);

        assertEquals(video1, video2);
        assertEquals(video1.hashCode(), video2.hashCode());
    }

    @Test
    void testUserVideosDTO_EqualityAndHashCode() {
        UserVideosDTO dto1 = new UserVideosDTO(4L, VideoStatus.PROCESSING_ERROR, "https://example.com/processing.mp4", 400L);
        UserVideosDTO dto2 = new UserVideosDTO(4L, VideoStatus.PROCESSING_ERROR, "https://example.com/processing.mp4", 400L);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}
