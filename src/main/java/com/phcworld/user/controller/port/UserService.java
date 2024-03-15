package com.phcworld.user.controller.port;

import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.UserRequest;

public interface UserService {
    User register(UserRequest requestUser);
    User getLoginUserInfo();
    User getUserInfo(Long userId);
    User modifyUserInfo(UserRequest requestDto);
    User delete(Long id);
}
