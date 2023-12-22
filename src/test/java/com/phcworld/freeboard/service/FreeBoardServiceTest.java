package com.phcworld.freeboard.service;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.exception.model.DuplicationException;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.exception.model.UnauthorizedException;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.dto.*;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.dto.UserResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FreeBoardServiceTest {

    @Mock
    private FreeBoardService freeBoardService;

    private static User user;

    @BeforeAll
    static void 회원_초기화(){
        user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .authority(Authority.ROLE_ADMIN)
                .createDate(LocalDateTime.now())
                .build();
    }

    @Test
    void 게시글_등록() {
        FreeBoardRequestDto request = FreeBoardRequestDto.builder()
                .title("title")
                .contents("contents")
                .build();
        FreeBoard board = FreeBoard.builder()
                .id(1L)
                .writer(user)
                .title(request.title())
                .contents(request.contents())
                .createDate(LocalDateTime.now())
                .build();
        FreeBoardResponseDto response = FreeBoardResponseDto.of(board);
        when(freeBoardService.registerFreeBoard(request)).thenReturn(response);
        FreeBoardResponseDto freeBoardResponse = freeBoardService.registerFreeBoard(request);
        assertThat(response).isEqualTo(freeBoardResponse);
    }

    @Test
    void 게시글_목록_조회(){
        FreeBoardSearchDto searchDto = FreeBoardSearchDto.builder()
                .pageNum(1)
                .pageSize(10)
                .searchType(0)
                .keyword("test")
                .build();
        FreeBoardSelectDto freeboard1 = FreeBoardSelectDto.builder()
                .id(1L)
                .writer(user)
                .title("title")
                .contents("contents")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .count(0)
                .build();

        FreeBoardSelectDto freeboard2 = FreeBoardSelectDto.builder()
                .id(2L)
                .writer(user)
                .title("title")
                .contents("contents")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .count(0)
                .build();
        List<FreeBoardSelectDto> list = new ArrayList<>();
        list.add(freeboard1);
        list.add(freeboard2);

        when(freeBoardService.getSearchList(searchDto)).thenReturn(list.stream()
                .map(FreeBoardResponseDto::of)
                .toList());
        List<FreeBoardResponseDto> result = freeBoardService.getSearchList(searchDto);
        assertThat(result).contains(FreeBoardResponseDto.of(freeboard1))
                .contains(FreeBoardResponseDto.of(freeboard2));
    }

    @Test
    void 게시글_하나_가져오기(){
        Map<String, Object> map = new HashMap<>();
        FreeBoardResponseDto responseDto = FreeBoardResponseDto.builder()
                .id(1L)
                .writer(UserResponseDto.of(user))
                .title("title")
                .contents("contents")
                .createDate("방금전")
                .count(1)
                .isNew(true)
                .build();
        map.put("freeboard", responseDto);
        map.put("isDeletedAuthrity", false);

//        when(freeBoardService.getFreeBoard(1L)).thenReturn(responseDto);
//        FreeBoardResponseDto freeBoardResponse = freeBoardService.getFreeBoard(1L);
//        assertThat(responseDto).isEqualTo(freeBoardResponse);

        when(freeBoardService.getFreeBoard(1L)).thenReturn(map);
        Map<String, Object> result = freeBoardService.getFreeBoard(1L);
        assertThat(result).isEqualTo(map);
    }

    @Test
    void 게시글_하나_가져오기_없음(){
        when(freeBoardService.getFreeBoard(1L)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            freeBoardService.getFreeBoard(1L);
        });
    }

    @Test
    void 게시글_수정(){
        FreeBoardRequestDto request = FreeBoardRequestDto.builder()
                .id(1L)
                .title("title")
                .contents("contents")
                .build();
        FreeBoardResponseDto responseDto = FreeBoardResponseDto.builder()
                .id(1L)
                .writer(UserResponseDto.of(user))
                .title("title")
                .contents("contents")
                .createDate("방금전")
                .count(1)
                .isNew(true)
                .build();

        when(freeBoardService.updateFreeBoard(request)).thenReturn(responseDto);
        FreeBoardResponseDto freeBoardResponse = freeBoardService.updateFreeBoard(request);
        assertThat(responseDto).isEqualTo(freeBoardResponse);
    }

    @Test
    void 게시글_수정_게시글_없음(){
        FreeBoardRequestDto request = FreeBoardRequestDto.builder()
                .id(1L)
                .title("title")
                .contents("contents")
                .build();
        when(freeBoardService.updateFreeBoard(request)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            freeBoardService.updateFreeBoard(request);
        });
    }

    @Test
    void 게시글_삭제(){
        SuccessResponseDto responseDto = SuccessResponseDto.builder()
                .statusCode(200)
                .message("삭제 성공")
                .build();

        when(freeBoardService.deleteFreeBoard(1L)).thenReturn(responseDto);
        SuccessResponseDto response = freeBoardService.deleteFreeBoard(1L);
        assertThat(responseDto).isEqualTo(response);
    }

    @Test
    void 게시글_삭제_실패_게시글_없음(){
        when(freeBoardService.deleteFreeBoard(1L)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            freeBoardService.deleteFreeBoard(1L);
        });
    }

    @Test
    void 게시글_삭제_실패_권한_없음(){
        when(freeBoardService.deleteFreeBoard(1L)).thenThrow(UnauthorizedException.class);
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            freeBoardService.deleteFreeBoard(1L);
        });
    }
}