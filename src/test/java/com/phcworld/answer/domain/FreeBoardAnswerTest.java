package com.phcworld.answer.domain;

import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.common.exception.model.DeletedEntityException;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FreeBoardAnswerTest {

    @Test
    @DisplayName("FreeBoardAnswerRequest, User, FreeBoard 정보로 생성할 수 있다.")
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
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .build();
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .updateDate(new FakeLocalDateTimeHolder(time).now())
                .isDeleteAuthority(true)
                .isModifyAuthority(true)
                .isDeleted(false)
                .build();
        FreeBoardAnswerRequest answerRequest = FreeBoardAnswerRequest.builder()
                .boardId(1L)
                .contents("답변내용")
                .build();

        // when
        FreeBoardAnswer result = FreeBoardAnswer.from(answerRequest, freeBoard, user, new FakeLocalDateTimeHolder(time));

        // then
        assertThat(result.getId()).isNull();
        assertThat(result.getContents()).isEqualTo("답변내용");
        assertThat(result.getCreateDate()).isEqualTo(time);
        assertThat(result.getFreeBoard().getTitle()).isEqualTo("제목");
        assertThat(result.getFreeBoard().getContents()).isEqualTo("내용");
        assertThat(result.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(result.isDeleted()).isFalse();
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
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .build();
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .updateDate(new FakeLocalDateTimeHolder(time).now())
                .isDeleteAuthority(true)
                .isModifyAuthority(true)
                .isDeleted(false)
                .build();
        FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
                .id(1L)
                .freeBoard(freeBoard)
                .writer(user)
                .contents("답변내용")
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .updateDate(new FakeLocalDateTimeHolder(time).now())
                .isDeleted(false)
                .build();

        // when
        boolean result = freeBoardAnswer.matchWriter(1L);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("내용을 수정할 수 있다.")
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
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .build();
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .updateDate(new FakeLocalDateTimeHolder(time).now())
                .isDeleteAuthority(true)
                .isModifyAuthority(true)
                .isDeleted(false)
                .build();
        FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
                .id(1L)
                .freeBoard(freeBoard)
                .writer(user)
                .contents("답변내용")
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .updateDate(new FakeLocalDateTimeHolder(time).now())
                .isDeleted(false)
                .build();

        // when
        FreeBoardAnswer result = freeBoardAnswer.update("답변내용수정");

        // then
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getContents()).isEqualTo("답변내용수정");
        assertThat(result.getCreateDate()).isEqualTo(time);
        assertThat(result.getFreeBoard().getTitle()).isEqualTo("제목");
        assertThat(result.getFreeBoard().getContents()).isEqualTo("내용");
        assertThat(result.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(result.isDeleted()).isFalse();
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
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .build();
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .updateDate(new FakeLocalDateTimeHolder(time).now())
                .isDeleteAuthority(true)
                .isModifyAuthority(true)
                .isDeleted(false)
                .build();
        FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
                .id(1L)
                .freeBoard(freeBoard)
                .writer(user)
                .contents("답변내용")
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .updateDate(new FakeLocalDateTimeHolder(time).now())
                .isDeleted(false)
                .build();

        // when
        FreeBoardAnswer result = freeBoardAnswer.delete();

        // then
        assertThat(result.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("이미 삭제된 답변은 이미 삭제되었다는 오류를 내려준다.")
    void failedDelete(){
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
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .build();
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .updateDate(new FakeLocalDateTimeHolder(time).now())
                .isDeleteAuthority(true)
                .isModifyAuthority(true)
                .isDeleted(false)
                .build();
        FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
                .id(1L)
                .freeBoard(freeBoard)
                .writer(user)
                .contents("답변내용")
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .updateDate(new FakeLocalDateTimeHolder(time).now())
                .isDeleted(true)
                .build();

        // when
        // then
        Assertions.assertThrows(DeletedEntityException.class, freeBoardAnswer::delete);
    }

}