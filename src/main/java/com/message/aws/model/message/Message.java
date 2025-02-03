package com.message.aws.model.message;

import com.message.aws.model.enums.VideoStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String user;
    private String videoUrl;
    private String createdDate;
    private String modifiedDate;
    @Enumerated(EnumType.STRING)
    private VideoStatus status;
}
