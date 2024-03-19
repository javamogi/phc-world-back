package com.phcworld.freeboard.service;

import com.phcworld.common.exception.model.DeletedEntityException;
import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.UnauthorizedException;
import com.phcworld.common.service.LocalDateTimeHolder;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.domain.dto.FreeBoardSearch;
import com.phcworld.mock.FakeAuthentication;
import com.phcworld.mock.FakeFreeBoardRepository;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.mock.FakeUserRepository;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FreeBoardServiceImplTest {

    private FreeBoardServiceImpl freeBoardService;

    @BeforeEach
    void init(){
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeFreeBoardRepository fakeFreeBoardRepository = new FakeFreeBoardRepository();
        LocalDateTimeHolder localDateTimeHolder = new FakeLocalDateTimeHolder(time);
        this.freeBoardService = FreeBoardServiceImpl.builder()
                .userRepository(fakeUserRepository)
                .freeBoardRepository(fakeFreeBoardRepository)
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
        fakeFreeBoardRepository.save(FreeBoard.builder()
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
                .build());

        fakeFreeBoardRepository.save(FreeBoard.builder()
                .id(2L)
                .title("제목2")
                .contents("내용2")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleteAuthority(false)
                .isModifyAuthority(false)
                .isDeleted(false)
                .build());

        fakeFreeBoardRepository.save(FreeBoard.builder()
                .id(3L)
                .title("안녕하세요.")
                .contents("잘부탁드립니다.")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleteAuthority(true)
                .isModifyAuthority(false)
                .isDeleted(false)
                .build());
        fakeFreeBoardRepository.save(FreeBoard.builder()
                .id(4L)
                .title("삭제테스트")
                .contents("삭제테스트를위한데이터")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleteAuthority(false)
                .isModifyAuthority(false)
                .isDeleted(true)
                .build());
    }

    @Test
    @DisplayName("회원은 게시글을 등록할 수 있다.")
    void successRegister(){
        // given
        FreeBoardRequest request = FreeBoardRequest.builder()
                .title("제목")
                .contents("내용")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        FreeBoard result = freeBoardService.register(request);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("제목");
        assertThat(result.getContents()).isEqualTo("내용");
        assertThat(result.getCount()).isZero();
        assertThat(result.getCountOfAnswer()).isZero();
        assertThat(result.isDeleted()).isFalse();
        assertThat(result.getWriter().getEmail()).isEqualTo("test@test.test");
    }

    @Test
    @DisplayName("가입하지 않은 회원은 게시글을 등록할 수 없다.")
    void failedRegisterWhenNotFoundUser(){
        // given
        FreeBoardRequest request = FreeBoardRequest.builder()
                .title("제목")
                .contents("내용")
                .build();
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            freeBoardService.register(request);
        });
    }

    @Test
    @DisplayName("제목으로 검색해서 게시글 목록을 가져올 수 있다.")
    void getSearchListWhenSearchTitle(){
        // given
        FreeBoardSearch search = FreeBoardSearch.builder()
                .searchType(0)
                .keyword("제목")
                .pageNum(1)
                .pageSize(5)
                .build();

        // when
        List<FreeBoard> result = freeBoardService.getSearchList(search);

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("내용으로 검색해서 게시글 목록을 가져올 수 있다.")
    void getSearchListWhenSearchContent(){
        // given
        FreeBoardSearch search = FreeBoardSearch.builder()
                .searchType(1)
                .keyword("잘부탁")
                .pageNum(1)
                .pageSize(5)
                .build();

        // when
        List<FreeBoard> result = freeBoardService.getSearchList(search);

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("작성자의 이름으로 검색해서 게시글 목록을 가져올 수 있다.")
    void getSearchListWhenSearchWriterName(){
        // given
        FreeBoardSearch search = FreeBoardSearch.builder()
                .searchType(3)
                .keyword("테스트")
                .pageNum(1)
                .pageSize(5)
                .build();

        // when
        List<FreeBoard> result = freeBoardService.getSearchList(search);

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(4);
    }

    // searchType이 벗어난 값을 확인해야할까?

    @Test
    @DisplayName("회원은 게시글의 id로 게시글을 가져올 수 있다.")
    void getFreeBoard(){
        // given
        long id = 1;
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        FreeBoard result = freeBoardService.getFreeBoard(id);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getTitle()).isEqualTo("제목");
        assertThat(result.getContents()).isEqualTo("내용");
        assertThat(result.getCount()).isEqualTo(1);
        assertThat(result.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(result.getCreateDate()).isEqualTo(LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111));
        assertThat(result.isModifyAuthority()).isTrue();
        assertThat(result.isDeleteAuthority()).isTrue();
    }

    @Test
    @DisplayName("id의 게시글이 없는 경우 게시글을 가져올 수 없다.")
    void failedGetFreeBoardWhenNotFoundFreeBoard(){
        // given
        long id = 999;

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            freeBoardService.getFreeBoard(id);
        });
    }

    @Test
    @DisplayName("id의 게시글이 삭제된 경우 게시글을 가져올 수 없다.")
    void failedGetFreeBoardWhenDeletedFreeBoard(){
        // given
        long id = 4;

        // when
        // then
        Assertions.assertThrows(DeletedEntityException.class, () -> {
            freeBoardService.getFreeBoard(id);
        });
    }

    @Test
    @DisplayName("작성자는 게시글을 변경할 수 있다.")
    void update(){
        // given
        FreeBoardRequest request = FreeBoardRequest.builder()
                .id(1L)
                .title("제목수정")
                .contents("내용수정")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        FreeBoard result = freeBoardService.update(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getTitle()).isEqualTo("제목수정");
        assertThat(result.getContents()).isEqualTo("내용수정");
        assertThat(result.getCount()).isEqualTo(0);
        assertThat(result.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(result.getCreateDate()).isEqualTo(LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111));
        assertThat(result.isModifyAuthority()).isTrue();
        assertThat(result.isDeleteAuthority()).isTrue();
    }

    @Test
    @DisplayName("등록되지 않은 게시물은 수정할 수 없다.")
    void failedUpdateWhenNotFound(){
        // given
        FreeBoardRequest request = FreeBoardRequest.builder()
                .id(999L)
                .title("제목수정")
                .contents("내용수정")
                .build();

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            freeBoardService.update(request);
        });
    }

    @Test
    @DisplayName("이미 삭제된 게시물은 수정할 수 없다.")
    void failedUpdateWhenDeleted(){
        // given
        FreeBoardRequest request = FreeBoardRequest.builder()
                .id(4L)
                .title("제목수정")
                .contents("내용수정")
                .build();

        // when
        // then
        Assertions.assertThrows(DeletedEntityException.class, () -> {
            freeBoardService.update(request);
        });
    }

    @Test
    @DisplayName("작성자가 다르면 수정할 수 없다.")
    void failedUpdateWhenNotEqualWriter(){
        // given
        FreeBoardRequest request = FreeBoardRequest.builder()
                .id(1L)
                .title("제목수정")
                .contents("내용수정")
                .build();
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            freeBoardService.update(request);
        });
    }

    @Test
    @DisplayName("ID로 작성자는 삭제할 수 있다.")
    void delete(){
        // given
        long id = 1;
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        FreeBoard result = freeBoardService.delete(id);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getTitle()).isEqualTo("제목");
        assertThat(result.getContents()).isEqualTo("내용");
        assertThat(result.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(result.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("ID로 관리자는 삭제할 수 있다.")
    void deleteByAdmin(){
        // given
        long id = 1;
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_ADMIN).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        FreeBoard result = freeBoardService.delete(id);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getTitle()).isEqualTo("제목");
        assertThat(result.getContents()).isEqualTo("내용");
        assertThat(result.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(result.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("등록되지 않은 게시물은 삭제할 수 없다.")
    void failedDelteWhenNotFound(){
        // given
        long id = 999;

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            freeBoardService.delete(id);
        });
    }

    @Test
    @DisplayName("이미 삭제된 게시물은 삭제할 수 없다.")
    void failedDeleteWhenDeleted(){
        // given
        long id = 4;

        // when
        // then
        Assertions.assertThrows(DeletedEntityException.class, () -> {
            freeBoardService.delete(id);
        });
    }

    @Test
    @DisplayName("작성자가 다르면 삭제할 수 없다.")
    void failedDeletedWhenNotEqualWriter(){
        // given
        long id = 1;
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            freeBoardService.delete(id);
        });
    }

}