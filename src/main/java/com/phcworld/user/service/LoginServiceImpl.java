package com.phcworld.user.service;

import com.phcworld.common.jwt.TokenProviderImpl;
import com.phcworld.common.jwt.dto.TokenDto;
import com.phcworld.common.jwt.service.CustomUserDetailsService;
import com.phcworld.common.security.utils.SecurityUtil;
import com.phcworld.user.controller.port.LoginService;
import com.phcworld.user.domain.dto.LoginRequest;
import com.phcworld.user.service.port.TokenProvider;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Builder
public class LoginServiceImpl implements LoginService {

    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenDto tokenLogin(LoginRequest requestUser) {
        // 비밀번호 확인 + spring security 객체 생성 후 JWT 토큰 생성
        Authentication authentication = SecurityUtil.getAuthentication(requestUser, userDetailsService, passwordEncoder);

        // 토큰 발급
        return tokenProvider.generateTokenDto(authentication);
    }

    @Override
    public String logout() {
        return "로그아웃";
    }

    @Override
    public TokenDto getNewToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return tokenProvider.generateTokenDto(authentication);
    }
}
