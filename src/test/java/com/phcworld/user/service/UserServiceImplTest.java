package com.phcworld.user.service;

import com.phcworld.common.exception.model.DeletedEntityException;
import com.phcworld.common.exception.model.DuplicationException;
import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.UnauthorizedException;
import com.phcworld.mock.FakeAuthentication;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.mock.FakePasswordEncode;
import com.phcworld.mock.FakeUserRepository;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.UserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceImplTest {

    private UserServiceImpl userService;

    @BeforeEach
    void init(){
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakePasswordEncode fakePasswordEncode = new FakePasswordEncode("test2");
        FakeLocalDateTimeHolder fakeLocalDateTimeHolder = new FakeLocalDateTimeHolder(
                LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111));
        this.userService = UserServiceImpl.builder()
                .userRepository(fakeUserRepository)
                .passwordEncoder(fakePasswordEncode)
                .localDateTimeHolder(fakeLocalDateTimeHolder)
                .build();

        fakeUserRepository.save(User.builder()
                        .id(1L)
                        .email("test@test.test")
                        .name("테스트")
                        .password("test")
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
    @DisplayName("회원가입 성공")
    void register() {
        // given
        UserRequest requestDto = UserRequest.builder()
                .email("test3@test.test")
                .password("test3")
                .name("테스트3")
                .build();

        // when
        User result = userService.register(requestDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test3@test.test");
        assertThat(result.getPassword()).isEqualTo("test2");
        assertThat(result.getName()).isEqualTo("테스트3");
        assertThat(result.getProfileImage()).isEqualTo("blank-profile-picture.png");
        assertThat(result.getAuthority()).isEqualTo(Authority.ROLE_USER);
        assertThat(result.getCreateDate()).isEqualTo(LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111));
    }

    @Test
    @DisplayName("회원가입 실패 가입된 이메일")
    void failedRegisterWhenDuplicateEmail(){
        // given
        UserRequest requestDto = UserRequest.builder()
                .email("test@test.test")
                .password("test")
                .name("test")
                .build();

        // when
        // then
        Assertions.assertThrows(DuplicationException.class, () -> {
            userService.register(requestDto);
        });
    }

    @Test
    @DisplayName("로그인 회원정보 가져오기")
    void getLoginUserInfo(){
        // given
        Authentication authentication = new FakeAuthentication(1).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        User result = userService.getLoginUserInfo();

        // then
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getEmail()).isEqualTo("test@test.test");
    }

    @Test
    @DisplayName("요청 회원정보 가져오기")
    void getUserInfo(){
        // given
        long id = 1;

        // when
        User result = userService.getUserInfo(id);

        // then
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getEmail()).isEqualTo("test@test.test");
    }

    @Test
    @DisplayName("회원 정보 요청 실패 가입하지 않은 회원")
    void failedGetUserInfo(){
        // given
        long id = 999;

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.getUserInfo(id);
        });
    }

    @Test
    @DisplayName("회원 정보 변경 성공")
    void modifyUserInfo() {
        // given
        UserRequest requestDto = UserRequest.builder()
                .id(1L)
                .email("test@test.test")
                .name("이름")
                .password("test2")
                .build();
        Authentication authentication = new FakeAuthentication(1).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        User result = userService.modifyUserInfo(requestDto);

        // then
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getEmail()).isEqualTo("test@test.test");
        assertThat(result.getName()).isEqualTo("이름");
        assertThat(result.getPassword()).isEqualTo("test2");
    }

    @Test
    @DisplayName("회원정보 변경 실패 로그인 회원 요청 회원 다름")
    void failedLoginWhenDifferentUser(){
        // given
        UserRequest requestDto = UserRequest.builder()
                .id(2L)
                .email("test2@test.test")
                .name("헤헤")
                .password("test2")
                .build();
        Authentication authentication = new FakeAuthentication(1).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            userService.modifyUserInfo(requestDto);
        });
    }

    @Test
    @DisplayName("회원 정보 변경 실패 없는 회원")
    void failedLoginWhenNotFoundUser(){
        // given
        UserRequest requestDto = UserRequest.builder()
                .id(999L)
                .email("test@test.test")
                .name("test")
                .password("test")
                .build();
        Authentication authentication = new FakeAuthentication(999).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.modifyUserInfo(requestDto);
        });
    }

    @Test
    @DisplayName("회원 정보 삭제 성공")
    void successDelete(){
        // given
        long id = 1;
        Authentication authentication = new FakeAuthentication(id).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        User result = userService.delete(id);

        // then
        assertThat(result.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("회원 정보 삭제 실패 로그인 회원 요청 회원 다름")
    void failedDeleteWhenDifferentUser(){
        // given
        Long id = 1L;
        Authentication authentication = new FakeAuthentication(2L).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            userService.delete(id);
        });
    }

    @Test
    @DisplayName("회원 정보 삭제 실패 회원 없음")
    void failedDeleteWhenNotFoundUser(){
        // given
        Long id = 999L;
        Authentication authentication = new FakeAuthentication(999L).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.delete(id);
        });
    }

    @Test
    @DisplayName("회원 정보 삭제 실패 이미 삭제된 회원")
    void failedDeleteWhenDeletedUser(){
        // given
        Long id = 2L;
        Authentication authentication = new FakeAuthentication(2L).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(DeletedEntityException.class, () -> {
            userService.delete(id);
        });
    }

}