package com.message.aws.model.entity;

import com.message.aws.model.enums.VideoStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private VideoStatus status;
    private String videoUrl;
    private String createdDate;
    private String modifiedDate;
    private Long userId;
}
