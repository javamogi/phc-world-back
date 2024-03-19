package com.phcworld.medium;

import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.UnauthorizedException;
import com.phcworld.freeboard.controller.port.FreeBoardResponse;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.domain.dto.FreeBoardSearch;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelect;
import com.phcworld.freeboard.service.FreeBoardServiceImpl;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FreeBoardServiceTest {

    @Mock
    private FreeBoardServiceImpl freeBoardService;

    private static UserEntity user;

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
    }

//    @Test
//    void 게시글_등록() {
//        FreeBoardRequest request = FreeBoardRequest.builder()
//                .title("title")
//                .contents("contents")
//                .build();
//        FreeBoardEntity board = FreeBoardEntity.builder()
//                .id(1L)
//                .writer(user)
//                .title(request.title())
//                .contents(request.contents())
//                .createDate(LocalDateTime.now())
//                .build();
//        FreeBoardResponse response = FreeBoardResponse.from(board);
//        when(freeBoardService.register(request)).thenReturn(response);
//        FreeBoardResponse freeBoardResponse = freeBoardService.register(request);
//        assertThat(response).isEqualTo(freeBoardResponse);
//    }

//    @Test
//    void 게시글_목록_조회(){
//        FreeBoardSearch searchDto = FreeBoardSearch.builder()
//                .pageNum(1)
//                .pageSize(10)
//                .searchType(0)
//                .keyword("test")
//                .build();
//        FreeBoardSelect freeboard1 = FreeBoardSelect.builder()
//                .id(1L)
//                .writer(user)
//                .title("title")
//                .contents("contents")
//                .createDate(LocalDateTime.now())
//                .updateDate(LocalDateTime.now())
//                .count(0)
//                .isDeleted(false)
//                .build();
//
//        FreeBoardSelect freeboard2 = FreeBoardSelect.builder()
//                .id(2L)
//                .writer(user)
//                .title("title")
//                .contents("contents")
//                .createDate(LocalDateTime.now())
//                .updateDate(LocalDateTime.now())
//                .count(0)
//                .isDeleted(false)
//                .build();
//        List<FreeBoardSelect> list = new ArrayList<>();
//        list.add(freeboard1);
//        list.add(freeboard2);
//
//        when(freeBoardService.getSearchList(searchDto)).thenReturn(
//                list.stream()
//                .map(FreeBoardResponse::from)
//                .toList());
//        List<FreeBoardResponse> result = freeBoardService.getSearchList(searchDto);
//        assertThat(result).contains(FreeBoardResponse.from(freeboard1))
//                .contains(FreeBoardResponse.from(freeboard2));
//    }

//    @Test
//    void 게시글_하나_가져오기(){
//        Map<String, Object> map = new HashMap<>();
//        FreeBoardResponse responseDto = FreeBoardResponse.builder()
//                .id(1L)
//                .writer(UserResponse.from(user.toModel()))
//                .title("title")
//                .contents("contents")
//                .createDate("방금전")
//                .count(1)
//                .isNew(true)
//                .build();
//        map.put("freeboard", responseDto);
//        map.put("isDeletedAuthrity", false);
//        map.put("isModifyAuthrity", false);
//
//        when(freeBoardService.getFreeBoard(1L)).thenReturn(map);
//        Map<String, Object> result = freeBoardService.getFreeBoard(1L);
//        assertThat(result).isEqualTo(map);
//    }

    @Test
    void 게시글_하나_가져오기_없음(){
        when(freeBoardService.getFreeBoard(1L)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            freeBoardService.getFreeBoard(1L);
        });
    }

//    @Test
//    void 게시글_수정(){
//        FreeBoardRequest request = FreeBoardRequest.builder()
//                .id(1L)
//                .title("title")
//                .contents("contents")
//                .build();
//        FreeBoardResponse responseDto = FreeBoardResponse.builder()
//                .id(1L)
//                .writer(UserResponse.from(user.toModel()))
//                .title("title")
//                .contents("contents")
//                .createDate("방금전")
//                .count(1)
//                .isNew(true)
//                .build();
//
//        when(freeBoardService.update(request)).thenReturn(responseDto);
//        FreeBoardResponse freeBoardResponse = freeBoardService.update(request);
//        assertThat(responseDto).isEqualTo(freeBoardResponse);
//    }

    @Test
    void 게시글_수정_게시글_없음(){
        FreeBoardRequest request = FreeBoardRequest.builder()
                .id(1L)
                .title("title")
                .contents("contents")
                .build();
        when(freeBoardService.update(request)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            freeBoardService.update(request);
        });
    }

//    @Test
//    void 게시글_삭제(){
//        SuccessResponseDto responseDto = SuccessResponseDto.builder()
//                .statusCode(200)
//                .message("삭제 성공")
//                .build();
//
//        when(freeBoardService.delete(1L)).thenReturn(responseDto);
//        SuccessResponseDto response = freeBoardService.delete(1L);
//        assertThat(responseDto).isEqualTo(response);
//    }

    @Test
    void 게시글_삭제_실패_게시글_없음(){
        when(freeBoardService.delete(100L)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            freeBoardService.delete(100L);
        });
    }

    @Test
    void 게시글_삭제_실패_권한_없음(){
        when(freeBoardService.delete(1L)).thenThrow(UnauthorizedException.class);
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            freeBoardService.delete(1L);
        });
    }
}