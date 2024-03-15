package com.phcworld.user.service.port;

import com.phcworld.common.jwt.dto.TokenDto;
import org.springframework.security.core.Authentication;

public interface TokenProvider {
    boolean validateToken(String token);
    TokenDto generateTokenDto(Authentication authentication);
    Authentication getAuthentication(String accessToken);
    String generateAccessToken(Authentication authentication, long now);
    String generateRefreshToken(Authentication authentication, long now);
}
