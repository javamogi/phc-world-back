package com.phcworld.answer.service;

import com.phcworld.answer.controller.port.FreeBoardAnswerService;
import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.common.exception.model.DeletedEntityException;
import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.NotMatchUserException;
import com.phcworld.common.exception.model.UnauthorizedException;
import com.phcworld.common.service.LocalDateTimeHolder;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.mock.*;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FreeBoardAnswerServiceImplTest {

    private FreeBoardAnswerService freeBoardAnswerService;

    private final LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);

    @BeforeEach
    void init() {
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeFreeBoardRepository fakeFreeBoardRepository = new FakeFreeBoardRepository();
        FakeFreeBoardAnswerRepository fakeFreeBoardAnswerRepository = new FakeFreeBoardAnswerRepository();
        LocalDateTimeHolder localDateTimeHolder = new FakeLocalDateTimeHolder(time);
        this.freeBoardAnswerService = FreeBoardAnswerServiceImpl.builder()
                .userRepository(fakeUserRepository)
                .freeBoardRepository(fakeFreeBoardRepository)
                .freeBoardAnswerRepository(fakeFreeBoardAnswerRepository)
                .localDateTimeHolder(localDateTimeHolder)
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
        fakeUserRepository.save(user);
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
        fakeFreeBoardRepository.save(freeBoard);
        FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
                .id(1L)
                .freeBoard(freeBoard)
                .writer(user)
                .contents("답변내용")
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .updateDate(new FakeLocalDateTimeHolder(time).now())
                .isDeleted(false)
                .build();
        fakeFreeBoardAnswerRepository.save(freeBoardAnswer);
        FreeBoardAnswer deletedFreeBoardAnswer = FreeBoardAnswer.builder()
                .id(2L)
                .freeBoard(freeBoard)
                .writer(user)
                .contents("삭제된 답변입니다.")
                .createDate(new FakeLocalDateTimeHolder(time).now())
                .updateDate(new FakeLocalDateTimeHolder(time).now())
                .isDeleted(true)
                .build();
        fakeFreeBoardAnswerRepository.save(deletedFreeBoardAnswer);
    }

    @Test
    @DisplayName("회원은 답변을 등록할 수 있다.")
    void successRegister(){
        // given
        FreeBoardAnswerRequest request = FreeBoardAnswerRequest.builder()
                .boardId(1L)
                .contents("답변내용")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        FreeBoardAnswer result = freeBoardAnswerService.register(request);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContents()).isEqualTo("답변내용");
        assertThat(result.isDeleted()).isFalse();
        assertThat(result.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(result.getFreeBoard().getTitle()).isEqualTo("제목");
    }

    @Test
    @DisplayName("등록되지 않은 게시글에 답변을 등록할 수 없다.")
    void failedRegisterWhenNotFoundFreeBoard(){
        // given
        FreeBoardAnswerRequest request = FreeBoardAnswerRequest.builder()
                .boardId(2L)
                .contents("답변내용")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            freeBoardAnswerService.register(request);
        });
    }

    @Test
    @DisplayName("가입하지 않은 회원은 답변을 등록할 수 없다.")
    void failedRegisterWhenNotFoundUser(){
        // given
        FreeBoardAnswerRequest request = FreeBoardAnswerRequest.builder()
                .boardId(1L)
                .contents("답변내용")
                .build();
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            freeBoardAnswerService.register(request);
        });
    }

    @Test
    @DisplayName("FreeBoardAnswerRequest로 답변을 수정할 수 있다.")
    void update(){
        // given
        FreeBoardAnswerRequest request = FreeBoardAnswerRequest.builder()
                .answerId(1L)
                .contents("답변내용수정")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        FreeBoardAnswer result = freeBoardAnswerService.update(request);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContents()).isEqualTo("답변내용수정");
        assertThat(result.isDeleted()).isFalse();
        assertThat(result.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(result.getFreeBoard().getTitle()).isEqualTo("제목");
    }

    @Test
    @DisplayName("id의 답변이 없는 경우 수정할 수 없다.")
    void failedUpdateWhenNotFound(){
        // given
        FreeBoardAnswerRequest request = FreeBoardAnswerRequest.builder()
                .answerId(3L)
                .contents("답변내용수정")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            freeBoardAnswerService.update(request);
        });
    }
    @Test
    @DisplayName("작성자가 다를 경우 수정할 수 없다.")
    void failedUpdateWhenNotMatchWriter(){
        // given
        FreeBoardAnswerRequest request = FreeBoardAnswerRequest.builder()
                .answerId(1L)
                .contents("답변내용수정")
                .build();
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(NotMatchUserException.class, () -> {
            freeBoardAnswerService.update(request);
        });
    }

    @Test
    @DisplayName("답변의 작성자는 삭제할 수 없다.")
    void delete(){
        // given
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        FreeBoardAnswer result = freeBoardAnswerService.delete(1L);

        // then
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("관리자는 삭제할 수 없다.")
    void deleteByAdmin(){
        // given
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_ADMIN).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        FreeBoardAnswer result = freeBoardAnswerService.delete(1L);

        // then
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("등록되지 않은 답변은 삭제할 수 없다.")
    void failedDeleteWhenNotFound(){
        // given
        long id = 999;

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            freeBoardAnswerService.delete(id);
        });
    }

    @Test
    @DisplayName("작성자가 다르면 삭제할 수 없다.")
    void failedDeleteWhenUnauthorization(){
        // given
        long id = 1;
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            freeBoardAnswerService.delete(id);
        });
    }

    @Test
    @DisplayName("삭제된 답변은 다시 삭제할 수 없다.")
    void failedDeleteWhenDeleted(){
        // given
        long id = 2;
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(DeletedEntityException.class, () -> {
            freeBoardAnswerService.delete(id);
        });
    }

}