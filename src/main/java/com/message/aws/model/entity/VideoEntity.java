package com.message.aws.model.entity;

import com.message.aws.model.enums.VideoStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "video")
public class VideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "video_generator")
    @SequenceGenerator(name = "video_generator", sequenceName = "video_id_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VideoStatus videoStatus;

    @Column(name = "url")
    private String videoUrl;

    @Column(name = "user-id")
    private Long userId;
}
