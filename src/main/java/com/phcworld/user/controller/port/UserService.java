package com.phcworld.user.controller.port;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.domain.dto.UserResponse;
import com.phcworld.user.infrastructure.UserEntity;

public interface UserService {
    UserResponse register(UserRequest requestUser);
    UserResponse getLoginUserInfo();
    UserResponse getUserInfo(Long userId);
    UserResponse modifyUserInfo(UserRequest requestDto);
    SuccessResponseDto delete(Long id);
}
