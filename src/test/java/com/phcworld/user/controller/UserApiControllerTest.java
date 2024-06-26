package com.phcworld.user.controller;

import com.phcworld.common.exception.model.DeletedEntityException;
import com.phcworld.common.exception.model.DuplicationException;
import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.UnauthorizedException;
import com.phcworld.common.utils.LocalDateTimeUtils;
import com.phcworld.mock.FakeAuthentication;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.mock.TestContainer;
import com.phcworld.user.controller.port.UserResponse;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.UserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserApiControllerTest {

    @Test
    @DisplayName("사용자는_회원가입을_할_수_있고_사용자의_권한은_ROLE_USER_이다")
    void successRegister(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        UserRequest requestDto = UserRequest.builder()
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .build();

        // when
        ResponseEntity<UserResponse> result = testContainer.userApiController.create(requestDto);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().email()).isEqualTo("test@test.test");
        assertThat(result.getBody().name()).isEqualTo("테스트");
        assertThat(result.getBody().createDate()).isEqualTo(LocalDateTimeUtils.getTime(time));
        assertThat(result.getBody().profileImage()).isEqualTo("blank-profile-picture.png");
    }

    @Test
    @DisplayName("회원가입 실패 가입된 이메일")
    void failedRegisterWhenDuplicateEmail(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111))
                .build());
        UserRequest requestDto = UserRequest.builder()
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .build();

        // when
        // then
        Assertions.assertThrows(DuplicationException.class, () -> {
            testContainer.userApiController.create(requestDto);
        });
    }

    @Test
    @DisplayName("로그인 회원정보 가져오기")
    void getLoginUserInfo(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build());
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResponseEntity<UserResponse> result = testContainer.userApiController.getUserInfo();

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().email()).isEqualTo("test@test.test");
        assertThat(result.getBody().name()).isEqualTo("테스트");
        assertThat(result.getBody().profileImage()).isEqualTo("blank-profile-picture.png");
        assertThat(result.getBody().createDate()).isEqualTo(LocalDateTimeUtils.getTime(time));
    }

    @Test
    @DisplayName("요청 회원정보 가져오기")
    void getUserInfo(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build());
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResponseEntity<UserResponse> result = testContainer.userApiController.getUserInfo(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().id()).isEqualTo(1);
        assertThat(result.getBody().email()).isEqualTo("test@test.test");
        assertThat(result.getBody().name()).isEqualTo("테스트");
        assertThat(result.getBody().profileImage()).isEqualTo("blank-profile-picture.png");
        assertThat(result.getBody().createDate()).isEqualTo(LocalDateTimeUtils.getTime(time));
    }

    @Test
    @DisplayName("회원 정보 요청 실패 가입하지 않은 회원")
    void failedGetUserInfo(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            testContainer.userApiController.getUserInfo(1L);
        });
    }

    @Test
    @DisplayName("회원 정보 변경 성공")
    void modifyUserInfo() {
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build());
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserRequest requestDto = UserRequest.builder()
                .id(1L)
                .email("test@test.test")
                .password("test2")
                .name("테스트2")
                .build();

        // when
        ResponseEntity<UserResponse> result = testContainer.userApiController.updateUser(requestDto);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().id()).isEqualTo(1);
        assertThat(result.getBody().email()).isEqualTo("test@test.test");
        assertThat(result.getBody().name()).isEqualTo("테스트2");
        assertThat(result.getBody().profileImage()).isEqualTo("blank-profile-picture.png");
        assertThat(result.getBody().createDate()).isEqualTo(LocalDateTimeUtils.getTime(time));
    }

    @Test
    @DisplayName("회원정보 변경 실패 로그인 회원 요청 회원 다름")
    void failedLoginWhenDifferentUser(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserRequest requestDto = UserRequest.builder()
                .id(1L)
                .email("test@test.test")
                .password("test2")
                .name("테스트2")
                .build();

        // when
        // then
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            testContainer.userApiController.updateUser(requestDto);
        });
    }

    @Test
    @DisplayName("회원 정보 변경 실패 없는 회원")
    void failedLoginWhenNotFoundUser(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserRequest requestDto = UserRequest.builder()
                .id(2L)
                .email("test2@test.test")
                .password("test2")
                .name("테스트2")
                .build();

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            testContainer.userApiController.updateUser(requestDto);
        });
    }

    @Test
    @DisplayName("회원 정보 삭제 성공")
    void successDelete(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build());
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResponseEntity<UserResponse> result = testContainer.userApiController.deleteUser(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().id()).isEqualTo(1L);
        assertThat(result.getBody().email()).isEqualTo("test@test.test");
        assertThat(result.getBody().name()).isEqualTo("테스트");
        assertThat(result.getBody().isDeleted()).isTrue();
    }

    @Test
    @DisplayName("관리자 권한으로 회원 정보 삭제 성공")
    void successDeleteWhenAdminRole(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test")
                .isDeleted(false)
                .authority(Authority.ROLE_ADMIN)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("test2@test.test")
                .name("테스트2")
                .password("test2")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build();
        testContainer.userRepository.save(user);
        testContainer.userRepository.save(user2);

        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_ADMIN).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResponseEntity<UserResponse> result = testContainer.userApiController.deleteUser(2L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().id()).isEqualTo(2L);
        assertThat(result.getBody().email()).isEqualTo("test2@test.test");
        assertThat(result.getBody().name()).isEqualTo("테스트2");
        assertThat(result.getBody().isDeleted()).isTrue();
    }

    @Test
    @DisplayName("회원 정보 삭제 실패 다른 회원")
    void failedDeleteWhenDifferentUser(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            testContainer.userApiController.deleteUser(1L);
        });
    }

    @Test
    @DisplayName("회원 정보 삭제 실패 회원 없음")
    void failedDeleteWhenNotFoundUser(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            testContainer.userApiController.deleteUser(1L);
        });
    }

    @Test
    @DisplayName("회원 정보 삭제 실패 이미 삭제된 회원")
    void failedDeleteWhenDeletedUser(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test")
                .isDeleted(true)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build());
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(DeletedEntityException.class, () -> {
            testContainer.userApiController.deleteUser(1L);
        });
    }

}