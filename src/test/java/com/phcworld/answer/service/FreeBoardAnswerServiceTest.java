package com.phcworld.answer.service;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.dto.FreeBoardAnswerRequestDto;
import com.phcworld.answer.dto.FreeBoardAnswerResponseDto;
import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.NotMatchUserException;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FreeBoardAnswerServiceTest {

    @Mock
    private FreeBoardAnswerService answerService;

    private static UserEntity user;

    private static FreeBoard board;

    @BeforeAll
    static void 회원_초기화(){
        user = UserEntity.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .authority(Authority.ROLE_ADMIN)
                .createDate(LocalDateTime.now())
                .build();

        board = FreeBoard.builder()
                .id(1L)
                .writer(user)
                .title("board title")
                .contents("board contents")
                .createDate(LocalDateTime.now())
                .build();
    }

    @Test
    void 답변_등록() {
        FreeBoardAnswerRequestDto request = FreeBoardAnswerRequestDto.builder()
                .boardId(1L)
                .contents("contents")
                .build();
        FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
                .id(1L)
                .writer(user)
                .freeBoard(board)
                .contents(request.contents())
                .build();

        FreeBoardAnswerResponseDto response = FreeBoardAnswerResponseDto.of(freeBoardAnswer);

        when(answerService.register(request)).thenReturn(response);
        FreeBoardAnswerResponseDto result = answerService.register(request);
        assertThat(response).isEqualTo(result);
    }

    @Test
    void 답변_등록_오류_없는_게시글() {
        FreeBoardAnswerRequestDto request = FreeBoardAnswerRequestDto.builder()
                .boardId(1L)
                .contents("contents")
                .build();
        when(answerService.register(request)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            answerService.register(request);
        });
    }

    @Test
    void 하나의_답변_조회() {
        FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
                .id(1L)
                .writer(user)
                .freeBoard(board)
                .contents("answer contents")
                .build();

        FreeBoardAnswerResponseDto response = FreeBoardAnswerResponseDto.of(freeBoardAnswer);

        when(answerService.getFreeBoardAnswer(1L)).thenReturn(response);
        FreeBoardAnswerResponseDto result = answerService.getFreeBoardAnswer(1L);
        assertThat(response).isEqualTo(result);
    }

    @Test
    void 하나의_답변_조회_오류_없는_답변() {
        when(answerService.getFreeBoardAnswer(1L)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            answerService.getFreeBoardAnswer(1L);
        });
    }

    @Test
    void 답변_수정_성공() {
        FreeBoardAnswerRequestDto request = FreeBoardAnswerRequestDto.builder()
                .answerId(1L)
                .contents("contents")
                .build();
        FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
                .id(1L)
                .writer(user)
                .freeBoard(board)
                .contents(request.contents())
                .build();

        FreeBoardAnswerResponseDto response = FreeBoardAnswerResponseDto.of(freeBoardAnswer);

        when(answerService.updateFreeBoardAnswer(request)).thenReturn(response);
        FreeBoardAnswerResponseDto result = answerService.updateFreeBoardAnswer(request);
        assertThat(response).isEqualTo(result);
    }

    @Test
    void 답변_수정_실패_없는_답변() {
        FreeBoardAnswerRequestDto request = FreeBoardAnswerRequestDto.builder()
                .answerId(1L)
                .contents("contents")
                .build();
        when(answerService.updateFreeBoardAnswer(request)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            answerService.updateFreeBoardAnswer(request);
        });
    }

    @Test
    void 답변_수정_실패_다른_작성자() {
        FreeBoardAnswerRequestDto request = FreeBoardAnswerRequestDto.builder()
                .answerId(1L)
                .contents("contents")
                .build();
        when(answerService.updateFreeBoardAnswer(request)).thenThrow(NotMatchUserException.class);
        Assertions.assertThrows(NotMatchUserException.class, () -> {
            answerService.updateFreeBoardAnswer(request);
        });
    }

    @Test
    void 답변_삭제_성공() {
        SuccessResponseDto response = SuccessResponseDto.builder()
                .message("삭제성공")
                .statusCode(200)
                .build();

        when(answerService.deleteFreeBoardAnswer(1L)).thenReturn(response);
        SuccessResponseDto result = answerService.deleteFreeBoardAnswer(1L);
        assertThat(response).isEqualTo(result);
    }
}