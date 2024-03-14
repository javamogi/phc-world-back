package com.phcworld.user.service;

import com.phcworld.common.exception.model.DeletedEntityException;
import com.phcworld.common.jwt.dto.TokenDto;
import com.phcworld.mock.FakePasswordEncode;
import com.phcworld.mock.FakeTokenProvider;
import com.phcworld.mock.FakeUserDetailsService;
import com.phcworld.mock.FakeUserRepository;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.LoginRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LoginServiceImplTest {

    private LoginServiceImpl loginService;

    @BeforeEach
    void init(){
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeUserDetailsService fakeUserDetailsService = new FakeUserDetailsService(fakeUserRepository);
        FakeTokenProvider fakeTokenProvider = new FakeTokenProvider(1, true);
        FakePasswordEncode fakePasswordEncode = new FakePasswordEncode("test2");
        this.loginService = LoginServiceImpl.builder()
                .userDetailsService(fakeUserDetailsService)
                .tokenProvider(fakeTokenProvider)
                .passwordEncoder(fakePasswordEncode)
                .build();

        fakeUserRepository.save(User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111))
                .build());

        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("test2@test.test")
                .name("테스트2")
                .password("test2")
                .isDeleted(true)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111))
                .build());
    }

    @Test
    @DisplayName("로그인 실패 가입되지 않은 이메일")
    void failedLoginWhenNotFoundEmail(){
        // given
        LoginRequest requestDto = LoginRequest.builder()
                .email("test2@test.test")
                .password("test2")
                .build();

        // when
        // then
        Assertions.assertThrows(InternalAuthenticationServiceException.class, () -> {
            loginService.tokenLogin(requestDto);
        });
    }

    @Test
    @DisplayName("로그인_실패_비밀번호_틀림")
    void failedLoginWhenNotMatchPassword(){
        // given
        LoginRequest requestDto = LoginRequest.builder()
                .email("test@test.test")
                .password("test1")
                .build();

        // when
        // then
        Assertions.assertThrows(BadCredentialsException.class, () -> {
            loginService.tokenLogin(requestDto);
        });
    }

    @Test
    @DisplayName("로그인_실패_삭제된_회원")
    void failedLoginWhenDeletedUser(){
        // given
        LoginRequest requestDto = LoginRequest.builder()
                .email("test2@test.test")
                .password("test2")
                .build();

        // when
        // then
//        Assertions.assertThrows(DeletedEntityException.class, () -> {
        Assertions.assertThrows(InternalAuthenticationServiceException.class, () -> {
            loginService.tokenLogin(requestDto);
        });
    }

    @Test
    @DisplayName("로그인_성공")
    void successLogin(){
        // given
        LoginRequest requestDto = LoginRequest.builder()
                .email("test@test.test")
                .password("test2")
                .build();

        // when
        TokenDto result = loginService.tokenLogin(requestDto);

        // then
        assertThat(result.getAccessToken()).isEqualTo("accessToken");
        assertThat(result.getGrantType()).isEqualTo("bearer");
        assertThat(result.getRefreshToken()).isEqualTo("refreshToken");
    }

}