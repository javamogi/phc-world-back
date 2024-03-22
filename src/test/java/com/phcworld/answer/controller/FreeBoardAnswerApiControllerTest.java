package com.phcworld.answer.controller;

import com.phcworld.answer.controller.port.FreeBoardAnswerResponse;
import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.NotMatchUserException;
import com.phcworld.common.exception.model.UnauthorizedException;
import com.phcworld.common.utils.LocalDateTimeUtils;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.mock.FakeAuthentication;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.mock.TestContainer;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class FreeBoardAnswerApiControllerTest {

    @Test
    @DisplayName("회원은 게시글의 답변을 등록할 수 있다")
    void register() {
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(() -> time)
                .build();
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
        testContainer.userRepository.save(user);
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build();
        testContainer.freeBoardRepository.save(freeBoard);
        FreeBoardAnswerRequest requestDto = FreeBoardAnswerRequest.builder()
                .boardId(1L)
                .contents("contents")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResponseEntity<FreeBoardAnswerResponse> result = testContainer.freeBoardAnswerApiController.register(requestDto);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().contents()).isEqualTo("contents");
        assertThat(result.getBody().writer().email()).isEqualTo("test@test.test");
        assertThat(result.getBody().isDeleted()).isFalse();
    }

    @Test
    @DisplayName("가입하지 않은 회원은 게시글의 답변을 등록할 수 업다")
    void failedRegisterWhenNotFoundUser() {
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(() -> time)
                .build();
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
        testContainer.userRepository.save(user);
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build();
        testContainer.freeBoardRepository.save(freeBoard);
        FreeBoardAnswerRequest requestDto = FreeBoardAnswerRequest.builder()
                .boardId(1L)
                .contents("contents")
                .build();
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        assertThatThrownBy(() -> {
            testContainer.freeBoardAnswerApiController.register(requestDto);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("회원은 존재하지 않는 게시글의 답변을 등록할 수 없다")
    void failedRegisterWhenNotFoundFreeBoard() {
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(() -> time)
                .build();
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
        testContainer.userRepository.save(user);
        FreeBoardAnswerRequest requestDto = FreeBoardAnswerRequest.builder()
                .boardId(1L)
                .contents("contents")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        assertThatThrownBy(() -> {
            testContainer.freeBoardAnswerApiController.register(requestDto);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("회원은 본인이 작성한 답변을 FreeBoardAnswerRequest 요청으로 수정할 수 있다")
    void update() {
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(() -> time)
                .build();
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
        testContainer.userRepository.save(user);
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build();
        testContainer.freeBoardRepository.save(freeBoard);
        testContainer.freeBoardAnswerRepository.save(FreeBoardAnswer.builder()
                        .id(1L)
                        .freeBoard(freeBoard)
                        .writer(user)
                        .contents("contents")
                        .createDate(time)
                        .updateDate(time)
                        .isDeleted(false)
                        .build());
        FreeBoardAnswerRequest requestDto = FreeBoardAnswerRequest.builder()
                .answerId(1L)
                .contents("내용으로 수정")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResponseEntity<FreeBoardAnswerResponse> result = testContainer.freeBoardAnswerApiController.update(requestDto);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().contents()).isEqualTo("내용으로 수정");
        assertThat(result.getBody().updatedDate()).isEqualTo(LocalDateTimeUtils.getTime(time));
        assertThat(result.getBody().writer().email()).isEqualTo("test@test.test");
        assertThat(result.getBody().isDeleted()).isFalse();
    }

    @Test
    @DisplayName("회원은 존재하지 않는 게시글의 답변을 수정할 수 없다")
    void failedUpdateWhenNotFoundFreeBoard() {
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(() -> time)
                .build();
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
        testContainer.userRepository.save(user);
        FreeBoardAnswerRequest requestDto = FreeBoardAnswerRequest.builder()
                .answerId(1L)
                .contents("내용 수정")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        assertThatThrownBy(() -> {
            testContainer.freeBoardAnswerApiController.update(requestDto);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("작성자가 다른 답변을 수정할 수 없다")
    void failedUpdateWhenNotMatchWriter() {
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(() -> time)
                .build();
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
        testContainer.userRepository.save(user);
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build();
        testContainer.freeBoardRepository.save(freeBoard);
        testContainer.freeBoardAnswerRepository.save(FreeBoardAnswer.builder()
                .id(1L)
                .freeBoard(freeBoard)
                .writer(user)
                .contents("contents")
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        FreeBoardAnswerRequest requestDto = FreeBoardAnswerRequest.builder()
                .answerId(1L)
                .contents("내용 수정")
                .build();
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        assertThatThrownBy(() -> {
            testContainer.freeBoardAnswerApiController.update(requestDto);
        }).isInstanceOf(NotMatchUserException.class);
    }

    @Test
    @DisplayName("회원은 본인이 작성한 답변을 id 요청으로 삭제할 수 있다")
    void delete() {
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(() -> time)
                .build();
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
        testContainer.userRepository.save(user);
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build();
        testContainer.freeBoardRepository.save(freeBoard);
        testContainer.freeBoardAnswerRepository.save(FreeBoardAnswer.builder()
                .id(1L)
                .freeBoard(freeBoard)
                .writer(user)
                .contents("contents")
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResponseEntity<FreeBoardAnswerResponse> result = testContainer.freeBoardAnswerApiController.delete(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().updatedDate()).isEqualTo(LocalDateTimeUtils.getTime(time));
        assertThat(result.getBody().writer().email()).isEqualTo("test@test.test");
        assertThat(result.getBody().isDeleted()).isTrue();
    }

    @Test
    @DisplayName("관리자는 id 요청으로 답변을 삭제할 수 있다")
    void deleteFromAdmin() {
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(() -> time)
                .build();
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
        testContainer.userRepository.save(user);
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build();
        testContainer.freeBoardRepository.save(freeBoard);
        testContainer.freeBoardAnswerRepository.save(FreeBoardAnswer.builder()
                .id(1L)
                .freeBoard(freeBoard)
                .writer(user)
                .contents("contents")
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_ADMIN).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResponseEntity<FreeBoardAnswerResponse> result = testContainer.freeBoardAnswerApiController.delete(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().updatedDate()).isEqualTo(LocalDateTimeUtils.getTime(time));
        assertThat(result.getBody().writer().email()).isEqualTo("test@test.test");
        assertThat(result.getBody().isDeleted()).isTrue();
    }

    @Test
    @DisplayName("작성자는 존재하지 않는 답변을 삭제할 수 없다")
    void failedDeleteWhenNotFoundFreeBoard() {
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(() -> time)
                .build();
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
        testContainer.userRepository.save(user);
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        assertThatThrownBy(() -> {
            testContainer.freeBoardAnswerApiController.delete(1L);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("회원은 본인이 작성하지 않은 답변은 삭제할 수 없다")
    void failedDeleteWhenDifferentAnswer() {
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(() -> time)
                .build();
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
        testContainer.userRepository.save(user);
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build();
        testContainer.freeBoardRepository.save(freeBoard);
        testContainer.freeBoardAnswerRepository.save(FreeBoardAnswer.builder()
                .id(1L)
                .freeBoard(freeBoard)
                .writer(user)
                .contents("contents")
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        assertThatThrownBy(() -> {
            testContainer.freeBoardAnswerApiController.delete(1L);
        }).isInstanceOf(UnauthorizedException.class);
    }

}