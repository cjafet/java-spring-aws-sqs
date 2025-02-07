package com.message.aws.model.entity;

import com.message.aws.model.enums.VideoStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "video")
public class VideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "video_generator")
    private Long id;

    @Column(name = "status")
    private VideoStatus videoStatus;

    @Column(name = "url")
    private String videoUrl;

    @Column(name = "user-id")
    private Long userId;
}
