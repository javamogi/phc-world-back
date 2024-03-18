package com.phcworld.freeboard.domain;

import com.phcworld.common.exception.model.DeletedEntityException;
import com.phcworld.common.exception.model.DuplicationException;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelect;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.mock.FakePasswordEncode;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.infrastructure.UserEntity;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class FreeBoardTest {

    @Test
    @DisplayName("FreeBoardRequest 요청 정보로 생성할 수 있다.")
    void createByFreeBoardRequest(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build();
        FreeBoardRequest freeBoardRequest = FreeBoardRequest.builder()
                .title("제목")
                .contents("내용")
                .build();

        // when
        FreeBoard result = FreeBoard.from(freeBoardRequest, user, new FakeLocalDateTimeHolder(time));

        // then
        assertThat(result.getId()).isNull();
        assertThat(result.getTitle()).isEqualTo("제목");
        assertThat(result.getContents()).isEqualTo("내용");
        assertThat(result.getCount()).isZero();
        assertThat(result.getCountOfAnswer()).isZero();
        assertThat(result.isDeleted()).isFalse();
        assertThat(result.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(result.getCreateDate()).isEqualTo(time);
    }

    @Test
    @DisplayName("FreeBoardSelect 정보로 생성할 수 있다.")
    void createByFreeBoardSelect(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build();
        FreeBoardSelect freeBoardSelect = FreeBoardSelect.builder()
                .id(1L)
                .writer(UserEntity.from(user))
                .title("title")
                .contents("contents")
                .createDate(time)
                .updateDate(time)
                .count(0)
                .countOfAnswer(0L)
                .isDeleted(false)
                .build();

        // when
        FreeBoard result = FreeBoard.from(freeBoardSelect);

        // then
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getTitle()).isEqualTo("title");
        assertThat(result.getContents()).isEqualTo("contents");
        assertThat(result.getCount()).isZero();
        assertThat(result.getCountOfAnswer()).isZero();
        assertThat(result.isDeleted()).isFalse();
        assertThat(result.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(result.getCreateDate()).isEqualTo(time);
    }

    @Test
    @DisplayName("회원의 ID값으로 작성자와 같은지 확인할 수 있다.")
    void matchWriter(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build();
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleteAuthority(true)
                .isModifyAuthority(true)
                .isDeleted(false)
                .build();

        // when
        boolean result = freeBoard.matchUser(1L);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("조회수를 1 올린다.")
    void addCount(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build();
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleteAuthority(true)
                .isModifyAuthority(true)
                .isDeleted(false)
                .build();

        // when
        FreeBoard result = freeBoard.addCount();

        // then
        assertThat(result.getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("제목과 내용을 수정할 수 있다.")
    void update(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build();
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleteAuthority(true)
                .isModifyAuthority(true)
                .isDeleted(false)
                .build();

        // when
        FreeBoard result = freeBoard.update("수정된 제목", "수정된 내용");

        // then
        assertThat(result.getTitle()).isEqualTo("수정된 제목");
        assertThat(result.getContents()).isEqualTo("수정된 내용");
    }

    @Test
    @DisplayName("삭제 할 수 있다.(soft delete-논리삭제)")
    void delete(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build();
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleteAuthority(true)
                .isModifyAuthority(true)
                .isDeleted(false)
                .build();

        // when
        FreeBoard result = freeBoard.delete();

        // then
        assertThat(result.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("작성자는 수정,삭제 권한을 가질수 있다.")
    void getAuthoritiesWhenEqualWriter(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build();
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleteAuthority(false)
                .isModifyAuthority(false)
                .isDeleted(false)
                .build();

        // when
        FreeBoard result = freeBoard.setAuthorities(1L, Authority.ROLE_USER);

        // then
        assertThat(result.isModifyAuthority()).isTrue();
        assertThat(result.isDeleteAuthority()).isTrue();
    }

    @Test
    @DisplayName("관리자는 삭제 권한만 가질수 있다.")
    void getAuthoritiesWhenAdmin(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build();
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleteAuthority(false)
                .isModifyAuthority(false)
                .isDeleted(false)
                .build();

        // when
        FreeBoard result = freeBoard.setAuthorities(2L, Authority.ROLE_ADMIN);

        // then
        assertThat(result.isModifyAuthority()).isFalse();
        assertThat(result.isDeleteAuthority()).isTrue();
    }
}