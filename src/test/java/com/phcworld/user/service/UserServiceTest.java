package com.phcworld.user.service;

import com.phcworld.exception.model.DuplicationException;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.dto.LoginUserRequestDto;
import com.phcworld.user.dto.UserRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserService userService;

    @Test
    void 회원가입() {
        UserRequestDto requestDto = UserRequestDto.builder()
                .email("test3@test.test")
                .password("test3")
                .name("테스트3")
                .build();

        User user = User.builder()
                .email(requestDto.email())
                .password(requestDto.password())
                .name(requestDto.name())
                .profileImage("blank-profile-picture.png")
                .authority(Authority.ROLE_USER)
                .createDate(LocalDateTime.now())
                .build();

        when(userService.registerUser(requestDto)).thenReturn(user);
        User savedUser = userService.registerUser(requestDto);
        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    void 회원가입_실패_가입된_이메일(){
        UserRequestDto requestDto = UserRequestDto.builder()
                .email("test@test.test")
                .password("test")
                .name("test")
                .build();
        when(userService.registerUser(requestDto)).thenThrow(DuplicationException.class);
        Assertions.assertThrows(DuplicationException.class, () -> {
            userService.registerUser(requestDto);
        });
    }

    @Test
    void 로그인_실패_가입되지_않은_이메일(){
        LoginUserRequestDto requestDto = LoginUserRequestDto.builder()
                .email("test@test.test")
                .password("test")
                .build();
        when(userService.tokenLogin(requestDto)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.tokenLogin(requestDto);
        });
    }

    @Test
    void 로그인_실패_비밀번호_틀림(){
        LoginUserRequestDto requestDto = LoginUserRequestDto.builder()
                .email("test@test.test")
                .password("test1")
                .build();
        when(userService.tokenLogin(requestDto)).thenThrow(BadCredentialsException.class);
        Assertions.assertThrows(BadCredentialsException.class, () -> {
            userService.tokenLogin(requestDto);
        });
    }

}