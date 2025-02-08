package com.message.aws.core.model.entity;

import com.message.aws.core.model.enums.VideoStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "status")
public class StatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private VideoStatus status;
    private String videoKey;
    private String createdDate;
    private String modifiedDate;
    private Long userId;
    private Long videoId;
}
