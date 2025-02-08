package com.message.aws.core.model.dto;

import com.message.aws.core.model.enums.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDTO {
    private Long id;
    private VideoStatus status;
    private String videoKey;
    private String createdDate;
    private String modifiedDate;
    private Long userId;
    private Long videoId;
}
