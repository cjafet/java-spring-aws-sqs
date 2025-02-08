package com.message.aws.core.model.entity;

import com.message.aws.core.model.enums.VideoStatus;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void testUser_AllFields() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(2L);
        userEntity.setUsername("johndoe");
        userEntity.setEmail("johndoe@example.com");
        userEntity.setPassword("securepassword");
        userEntity.setAuthorities(List.of(new SimpleGrantedAuthority("ROLE_USER")));

        assertEquals(2L, userEntity.getId());
        assertEquals("johndoe", userEntity.getUsername());
        assertEquals("johndoe@example.com", userEntity.getEmail());
        assertEquals("securepassword", userEntity.getPassword());
        assertEquals(1, userEntity.getAuthorities().size());
        assertTrue(userEntity.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testVideoEntity_AllFields() {
        VideoEntity video = new VideoEntity();
        video.setId(3L);
        video.setVideoStatus(VideoStatus.IN_PROGRESS);
        video.setVideoKey("https://example.com/processing.mp4");
        video.setUserId(300L);

        assertEquals(3L, video.getId());
        assertEquals(VideoStatus.IN_PROGRESS, video.getVideoStatus());
        assertEquals("https://example.com/processing.mp4", video.getVideoKey());
        assertEquals(300L, video.getUserId());
    }

    @Test
    void testVideoEntity_EqualityAndHashCode() {
        VideoEntity video1 = new VideoEntity();
        video1.setId(5L);
        video1.setVideoStatus(VideoStatus.COMPLETED);
        video1.setVideoKey("https://example.com/completed.mp4");
        video1.setUserId(500L);

        VideoEntity video2 = new VideoEntity();
        video2.setId(5L);
        video2.setVideoStatus(VideoStatus.COMPLETED);
        video2.setVideoKey("https://example.com/completed.mp4");
        video2.setUserId(500L);

        assertEquals(video1, video2);
        assertEquals(video1.hashCode(), video2.hashCode());
    }
}
