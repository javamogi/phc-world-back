package com.phcworld.user.service;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.common.exception.model.DuplicationException;
import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.UnauthorizedException;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.domain.dto.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserServiceImpl userService;

//    @Test
//    void 회원가입() {
//        UserRequest requestDto = UserRequest.builder()
//                .email("test3@test.test")
//                .password("test3")
//                .name("테스트3")
//                .build();
//
//        UserEntity user = UserEntity.builder()
//                .email(requestDto.email())
//                .password(requestDto.password())
//                .name(requestDto.name())
//                .profileImage("blank-profile-picture.png")
//                .authority(Authority.ROLE_USER)
//                .createDate(LocalDateTime.now())
//                .build();
//
//        when(userService.register(requestDto)).thenReturn(user);
//        UserEntity savedUser = userService.register(requestDto);
//        assertThat(savedUser).isEqualTo(user);
//    }

    @Test
    void 회원가입_실패_가입된_이메일(){
        UserRequest requestDto = UserRequest.builder()
                .email("test@test.test")
                .password("test")
                .name("test")
                .build();
        when(userService.register(requestDto)).thenThrow(DuplicationException.class);
        Assertions.assertThrows(DuplicationException.class, () -> {
            userService.register(requestDto);
        });
    }

//    @Test
//    void 로그인_실패_가입되지_않은_이메일(){
//        LoginRequest requestDto = LoginRequest.builder()
//                .email("test@test.test")
//                .password("test")
//                .build();
//        when(userService.tokenLogin(requestDto)).thenThrow(NotFoundException.class);
//        Assertions.assertThrows(NotFoundException.class, () -> {
//            userService.tokenLogin(requestDto);
//        });
//    }

//    @Test
//    void 로그인_실패_비밀번호_틀림(){
//        LoginRequest requestDto = LoginRequest.builder()
//                .email("test@test.test")
//                .password("test1")
//                .build();
//        when(userService.tokenLogin(requestDto)).thenThrow(BadCredentialsException.class);
//        Assertions.assertThrows(BadCredentialsException.class, () -> {
//            userService.tokenLogin(requestDto);
//        });
//    }
//
//    @Test
//    void 로그인_실패_삭제된_회원(){
//        LoginRequest requestDto = LoginRequest.builder()
//                .email("test@test.test")
//                .password("test1")
//                .build();
//        when(userService.tokenLogin(requestDto)).thenThrow(DeletedEntityException.class);
//        Assertions.assertThrows(DeletedEntityException.class, () -> {
//            userService.tokenLogin(requestDto);
//        });
//    }

//    @Test
//    void 로그인_성공(){
//        LoginRequest requestDto = LoginRequest.builder()
//                .email("test@test.test")
//                .password("test")
//                .build();
//        TokenDto tokenDto = TokenDto.builder()
//                .grantType("grantType")
//                .accessToken("accessToken")
//                .refreshToken("refreshToken")
//                .build();
//        when(userService.tokenLogin(requestDto)).thenReturn(tokenDto);
//        TokenDto resultToken = userService.tokenLogin(requestDto);
//        assertThat(resultToken).isEqualTo(tokenDto);
//    }

    @Test
    void 로그인_회원_정보_가져오기(){
        UserResponse userResponseDto = UserResponse.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .createDate("방금전")
                .build();
        when(userService.getLoginUserInfo()).thenReturn(userResponseDto);
        UserResponse result = userService.getLoginUserInfo();
        assertThat(result).isEqualTo(userResponseDto);
    }

    @Test
    void 회원_정보_가져오기(){
        UserResponse userResponseDto = UserResponse.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .createDate("방금전")
                .build();
        when(userService.getUserInfo(1L)).thenReturn(userResponseDto);
        UserResponse result = userService.getUserInfo(1L);
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

        UserRequest requestDto = UserRequest.builder()
                .id(1L)
                .email("test@test.test")
                .name("test")
                .password("test")
                .imageData(imgData)
                .imageName("test.png")
                .build();

        UserResponse userResponseDto = UserResponse.builder()
                .id(1L)
                .email("test@test.test")
                .name("test")
                .createDate("방금전")
                .profileImage("imgUrl")
                .build();
        when(userService.modifyUserInfo(requestDto)).thenReturn(userResponseDto);
        UserResponse result = userService.modifyUserInfo(requestDto);
        assertThat(result).isEqualTo(userResponseDto);
    }

    @Test
    void 회원_정보_변경_실패_로그인_회원_요청_회원_다름(){
        UserRequest requestDto = UserRequest.builder()
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
        UserRequest requestDto = UserRequest.builder()
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

        when(userService.delete(1L)).thenReturn(successResponseDto);
        SuccessResponseDto result = userService.delete(1L);
        assertThat(result).isEqualTo(successResponseDto);
    }

    @Test
    void 회원_정보_삭제_실패_로그인_회원_요청_회원_다름(){
        Long id = 1L;
        when(userService.delete(id)).thenThrow(UnauthorizedException.class);

        Assertions.assertThrows(UnauthorizedException.class, () -> {
            userService.delete(id);
        });
    }

    @Test
    void 회원_정보_삭제_실패_회원_없음(){
        Long id = 1L;
        when(userService.delete(id)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.delete(id);
        });
    }

}