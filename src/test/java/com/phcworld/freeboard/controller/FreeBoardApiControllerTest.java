package com.phcworld.freeboard.controller;

import com.phcworld.common.exception.model.DeletedEntityException;
import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.UnauthorizedException;
import com.phcworld.common.utils.LocalDateTimeUtils;
import com.phcworld.freeboard.controller.port.FreeBoardResponse;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.domain.dto.FreeBoardSearch;
import com.phcworld.mock.FakeAuthentication;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.mock.TestContainer;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FreeBoardApiControllerTest {

    @Test
    @DisplayName("회원은 게시글을 등록할 수 있다.")
    void register(){
        // given
        LocalDateTime time = LocalDateTime.now();
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
        FreeBoardRequest request = FreeBoardRequest.builder()
                .title("제목")
                .contents("내용")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResponseEntity<FreeBoardResponse> result = testContainer.freeBoardApiController.register(request);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().title()).isEqualTo("제목");
        assertThat(result.getBody().contents()).isEqualTo("내용");
        assertThat(result.getBody().count()).isZero();
        assertThat(result.getBody().countOfAnswer()).isZero();
        assertThat(result.getBody().createDate()).isEqualTo(LocalDateTimeUtils.getTime(time));
        assertThat(result.getBody().isNew()).isTrue();
    }

    @Test
    @DisplayName("가입하지 않은 회원은 게시글을 등록할 수 없다.")
    void failedRegisterWhenNotFoundUser(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        FreeBoardRequest request = FreeBoardRequest.builder()
                .title("제목")
                .contents("내용")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            testContainer.freeBoardApiController.register(request);
        });
    }

    @Test
    @DisplayName("제목으로 검색해서 게시글 목록을 가져올 수 있다.")
    void getListWhenSearchTitle(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(2L)
                .title("안녕하세요")
                .contents("잘부탁드립니다")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        FreeBoardSearch search = FreeBoardSearch.builder()
                .searchType(0)
                .keyword("제목")
                .pageNum(1)
                .pageSize(5)
                .build();

        // when
        ResponseEntity<List<FreeBoardResponse>> result = testContainer.freeBoardApiController.getList(search);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody()).hasSize(1);
    }

    @Test
    @DisplayName("내용으로 검색해서 게시글 목록을 가져올 수 있다.")
    void getListWhenSearchContent(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(2L)
                .title("안녕하세요")
                .contents("잘부탁드립니다")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        FreeBoardSearch search = FreeBoardSearch.builder()
                .searchType(1)
                .keyword("내용")
                .pageNum(1)
                .pageSize(5)
                .build();

        // when
        ResponseEntity<List<FreeBoardResponse>> result = testContainer.freeBoardApiController.getList(search);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody()).hasSize(1);
    }

    @Test
    @DisplayName("작성자 이름으로 검색해서 게시글 목록을 가져올 수 있다.")
    void getListWhenSearchWriterName(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(2L)
                .title("안녕하세요")
                .contents("잘부탁드립니다")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        FreeBoardSearch search = FreeBoardSearch.builder()
                .searchType(3)
                .keyword("테스트")
                .pageNum(1)
                .pageSize(5)
                .build();

        // when
        ResponseEntity<List<FreeBoardResponse>> result = testContainer.freeBoardApiController.getList(search);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody()).hasSize(2);
    }

    @Test
    @DisplayName("회원은 게시글의 id로 게시글을 가져올 수 있다.")
    void getFreeBoard(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        long id = 1;
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResponseEntity<FreeBoardResponse> result = testContainer.freeBoardApiController.getFreeBoard(id);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().id()).isEqualTo(1);
        assertThat(result.getBody().title()).isEqualTo("제목");
        assertThat(result.getBody().contents()).isEqualTo("내용");
        assertThat(result.getBody().count()).isEqualTo(1);
        assertThat(result.getBody().isNew()).isTrue();
        assertThat(result.getBody().countOfAnswer()).isEqualTo(0);
        assertThat(result.getBody().createDate()).isEqualTo(LocalDateTimeUtils.getTime(time));
        assertThat(result.getBody().writer().email()).isEqualTo("test@test.test");
    }

    @Test
    @DisplayName("id의 게시글이 없는 경우 게시글을 가져올 수 없다.")
    void failedGetFreeBoardWhenNotFoundFreeBoard(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        long id = 1;
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            testContainer.freeBoardApiController.getFreeBoard(id);
        });
    }

    @Test
    @DisplayName("id의 게시글이 삭제된 경우 게시글을 가져올 수 없다.")
    void failedGetFreeBoardWhenDeletedFreeBoard(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(true)
                .build());
        long id = 1;
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(DeletedEntityException.class, () -> {
            testContainer.freeBoardApiController.getFreeBoard(id);
        });
    }

    @Test
    @DisplayName("작성자는 게시글을 변경할 수 있다.")
    void update(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        FreeBoardRequest request = FreeBoardRequest.builder()
                .id(1L)
                .title("제목수정")
                .contents("내용수정")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResponseEntity<FreeBoardResponse> result = testContainer.freeBoardApiController.update(request);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().id()).isEqualTo(1);
        assertThat(result.getBody().title()).isEqualTo("제목수정");
        assertThat(result.getBody().contents()).isEqualTo("내용수정");
        assertThat(result.getBody().count()).isZero();
        assertThat(result.getBody().isNew()).isTrue();
        assertThat(result.getBody().countOfAnswer()).isZero();
        assertThat(result.getBody().createDate()).isEqualTo(LocalDateTimeUtils.getTime(time));
        assertThat(result.getBody().writer().email()).isEqualTo("test@test.test");
    }

    @Test
    @DisplayName("등록되지 않은 게시물은 수정할 수 없다.")
    void failedUpdateWhenNotFound(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        FreeBoardRequest request = FreeBoardRequest.builder()
                .id(1L)
                .title("제목수정")
                .contents("내용수정")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            testContainer.freeBoardApiController.update(request);
        });
    }

    @Test
    @DisplayName("이미 삭제된 게시물은 수정할 수 없다.")
    void failedUpdateWhenDeleted(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(true)
                .build());
        FreeBoardRequest request = FreeBoardRequest.builder()
                .id(1L)
                .title("제목수정")
                .contents("내용수정")
                .build();
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(DeletedEntityException.class, () -> {
            testContainer.freeBoardApiController.update(request);
        });
    }

    @Test
    @DisplayName("작성자가 다르면 수정할 수 없다.")
    void failedUpdateWhenNotEqualWriter(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
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
            testContainer.freeBoardApiController.update(request);
        });
    }

    @Test
    @DisplayName("작성자는 삭제할 수 있다.")
    void delete(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        Authentication authentication = new FakeAuthentication(1, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResponseEntity<FreeBoardResponse> result = testContainer.freeBoardApiController.delete(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().isDelete()).isTrue();
    }

    @Test
    @DisplayName("관리자는 삭제할 수 있다.")
    void deleteByAdmin(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_ADMIN).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResponseEntity<FreeBoardResponse> result = testContainer.freeBoardApiController.delete(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().isDelete()).isTrue();
    }

    @Test
    @DisplayName("등록되지 않은 게시물은 삭제할 수 없다.")
    void failedDelteWhenNotFound(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_ADMIN).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            testContainer.freeBoardApiController.delete(1L);
        });
    }

    @Test
    @DisplayName("이미 삭제된 게시물은 삭제할 수 없다.")
    void failedDeleteWhenDeleted(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(true)
                .build());
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_ADMIN).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(DeletedEntityException.class, () -> {
            testContainer.freeBoardApiController.delete(1L);
        });
    }

    @Test
    @DisplayName("작성자가 다르면 삭제할 수 없다.")
    void failedDeletedWhenNotEqualWriter(){
        // given
        LocalDateTime time = LocalDateTime.now();
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
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
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .countOfAnswer(0)
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .isDeleted(false)
                .build());
        Authentication authentication = new FakeAuthentication(2, Authority.ROLE_USER).getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        // then
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            testContainer.freeBoardApiController.delete(1L);
        });
    }

}