package com.message.aws.model.dto;

import com.message.aws.model.enums.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVideosDTO {
    private Long id;
    private VideoStatus videoStatus;
    private String videoUrl;
    private Long userId;
}
