package com.phcworld.user.domain;

import com.phcworld.mock.FakePasswordEncode;
import com.phcworld.user.domain.dto.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("UserRequest 요청 정보로 생성할 수 있다.")
    void createByUserRequest(){
        // given
        UserRequest userRequest = UserRequest.builder()
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .build();
        LocalDateTime now = LocalDateTime.now();

        // when
        User user = User.from(userRequest, new FakePasswordEncode("test"), now);

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("test@test.test");
        assertThat(user.getPassword()).isEqualTo("test");
        assertThat(user.getName()).isEqualTo("테스트");
        assertThat(user.getAuthority()).isEqualTo(Authority.ROLE_USER);
        assertThat(user.getProfileImage()).isEqualTo("blank-profile-picture.png");
        assertThat(user.getCreateDate()).isEqualTo(now);
    }

    @Test
    @DisplayName("UserRequest 요청 정보로 수정할 수 있다.")
    void modifyByUserRequest(){
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(now)
                .authority(Authority.ROLE_USER)
                .isDeleted(false)
                .build();
        UserRequest userRequest = UserRequest.builder()
                .id(1L)
                .password("test2")
                .name("테스트2")
                .build();

        // when
        user = user.modify(userRequest, "test.png", new FakePasswordEncode("test2"));

        // then
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo("test@test.test");
        assertThat(user.getPassword()).isEqualTo("test2");
        assertThat(user.getName()).isEqualTo("테스트2");
        assertThat(user.getAuthority()).isEqualTo(Authority.ROLE_USER);
        assertThat(user.getProfileImage()).isEqualTo("test.png");
        assertThat(user.getCreateDate()).isEqualTo(now);
    }

    @Test
    @DisplayName("삭제 요청으로 삭제 값이 변경된다.")
    void delete(){
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(now)
                .authority(Authority.ROLE_USER)
                .isDeleted(false)
                .build();

        // when
        user.delete();

        // then
        assertThat(user.isDeleted()).isTrue();
    }

}