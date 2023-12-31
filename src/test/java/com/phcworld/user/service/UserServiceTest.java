package com.phcworld.user.service;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.exception.model.DeletedEntityException;
import com.phcworld.exception.model.DuplicationException;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.exception.model.UnauthorizedException;
import com.phcworld.jwt.dto.TokenDto;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.dto.LoginUserRequestDto;
import com.phcworld.user.dto.UserRequestDto;
import com.phcworld.user.dto.UserResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Base64;

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

    @Test
    void 로그인_실패_삭제된_회원(){
        LoginUserRequestDto requestDto = LoginUserRequestDto.builder()
                .email("test@test.test")
                .password("test1")
                .build();
        when(userService.tokenLogin(requestDto)).thenThrow(DeletedEntityException.class);
        Assertions.assertThrows(DeletedEntityException.class, () -> {
            userService.tokenLogin(requestDto);
        });
    }

    @Test
    void 로그인_성공(){
        LoginUserRequestDto requestDto = LoginUserRequestDto.builder()
                .email("test@test.test")
                .password("test")
                .build();
        TokenDto tokenDto = TokenDto.builder()
                .grantType("grantType")
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
        when(userService.tokenLogin(requestDto)).thenReturn(tokenDto);
        TokenDto resultToken = userService.tokenLogin(requestDto);
        assertThat(resultToken).isEqualTo(tokenDto);
    }

    @Test
    void 로그인_회원_정보_가져오기(){
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .createDate("방금전")
                .build();
        when(userService.getLoginUserInfo()).thenReturn(userResponseDto);
        UserResponseDto result = userService.getLoginUserInfo();
        assertThat(result).isEqualTo(userResponseDto);
    }

    @Test
    void 회원_정보_가져오기(){
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .createDate("방금전")
                .build();
        when(userService.getUserInfo(1L)).thenReturn(userResponseDto);
        UserResponseDto result = userService.getUserInfo(1L);
        assertThat(result).isEqualTo(userResponseDto);
    }

    @Test
    void 회원_정보_요청_없는_회원(){
        when(userService.getUserInfo(3L)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.getUserInfo(3L);
        });
    }

    @Test
    void 회원_정보_변경_성공() throws IOException {
        File file = new File("src/main/resources/static/image/PHC-WORLD.png");
        byte[] bytesFile = Files.readAllBytes(file.toPath());
        String imgData = Base64.getEncoder().encodeToString(bytesFile);

        UserRequestDto requestDto = UserRequestDto.builder()
                .id(1L)
                .email("test@test.test")
                .name("test")
                .password("test")
                .imageData(imgData)
                .imageName("test.png")
                .build();

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(1L)
                .email("test@test.test")
                .name("test")
                .createDate("방금전")
                .profileImage("imgUrl")
                .build();
        when(userService.modifyUserInfo(requestDto)).thenReturn(userResponseDto);
        UserResponseDto result = userService.modifyUserInfo(requestDto);
        assertThat(result).isEqualTo(userResponseDto);
    }

    @Test
    void 회원_정보_변경_실패_로그인_회원_요청_회원_다름(){
        UserRequestDto requestDto = UserRequestDto.builder()
                .id(1L)
                .email("test@test.test")
                .name("test")
                .password("test")
                .build();

        when(userService.modifyUserInfo(requestDto)).thenThrow(UnauthorizedException.class);
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            userService.modifyUserInfo(requestDto);
        });
    }

    @Test
    void 회원_정보_변경_실패_없는_회원(){
        UserRequestDto requestDto = UserRequestDto.builder()
                .id(1L)
                .email("test@test.test")
                .name("test")
                .password("test")
                .build();

        when(userService.modifyUserInfo(requestDto)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.modifyUserInfo(requestDto);
        });
    }

    @Test
    void 회원_정보_삭제_성공(){
        SuccessResponseDto successResponseDto = SuccessResponseDto.builder()
                .statusCode(200)
                .message("삭제 성공")
                .build();

        when(userService.deleteUser(1L)).thenReturn(successResponseDto);
        SuccessResponseDto result = userService.deleteUser(1L);
        assertThat(result).isEqualTo(successResponseDto);
    }

    @Test
    void 회원_정보_삭제_실패_로그인_회원_요청_회원_다름(){
        Long id = 1L;
        when(userService.deleteUser(id)).thenThrow(UnauthorizedException.class);

        Assertions.assertThrows(UnauthorizedException.class, () -> {
            userService.deleteUser(id);
        });
    }

    @Test
    void 회원_정보_삭제_실패_회원_없음(){
        Long id = 1L;
        when(userService.deleteUser(id)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.deleteUser(id);
        });
    }

}