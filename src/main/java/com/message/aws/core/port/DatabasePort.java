package com.message.aws.core.port;

import com.message.aws.core.model.dto.StatusDTO;
import com.message.aws.core.model.dto.UserDTO;
import com.message.aws.core.model.dto.UserVideosDTO;

import java.util.Optional;

public interface DatabasePort {
    StatusDTO saveOrUpdateVideoStatus(StatusDTO statusDTO);
    UserVideosDTO createVideo(UserVideosDTO userVideosDTO);
    Optional<UserDTO> getUserByEmail(String email);
    Optional<StatusDTO> getStatusByVideoId(Long videoId);
}
