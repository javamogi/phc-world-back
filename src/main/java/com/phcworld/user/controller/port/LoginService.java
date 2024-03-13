package com.phcworld.user.controller.port;

import com.phcworld.common.jwt.dto.TokenDto;
import com.phcworld.user.domain.dto.LoginRequest;

public interface LoginService {
    TokenDto tokenLogin(LoginRequest requestUser);
    String logout();
    TokenDto getNewToken();
}
