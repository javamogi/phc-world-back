package com.phcworld.freeboard.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phcworld.freeboard.dto.FreeBoardRequestDto;
import com.phcworld.freeboard.dto.FreeBoardSearchDto;
import com.phcworld.jwt.TokenProvider;
import com.phcworld.user.domain.Authority;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class FreeBoardApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean
    private TokenProvider tokenProvider;


    @Autowired
    private ObjectMapper objectMapper;

    private static String token;

    @BeforeEach
    void 토큰_생성(){
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{Authority.ROLE_ADMIN.toString()})
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        UserDetails principal = new org.springframework.security.core.userdetails.User("1", "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        long now = (new Date()).getTime();
        String accessToken = tokenProvider.generateAccessToken(authentication, now);
        token = "Bearer " + accessToken;
    }

    @Test
    void 게시글_등록_성공() throws Exception {
        FreeBoardRequestDto requestDto = FreeBoardRequestDto.builder()
                .title("title")
                .contents("contents")
                .build();
        String request = objectMapper.writeValueAsString(requestDto);

        this.mvc.perform(post("/api/freeboards")
                        .header("Authorization", token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 게시글_목록_조회() throws Exception {

        this.mvc.perform(get("/api/freeboards")
                        .header("Authorization", token)
                        .with(csrf())
                        .param("pageNum", "1")
                        .param("pageSize", "10")
                        .param("searchType", "0")
                        .param("keyword", ""))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 게시글_하나_조회() throws Exception {

        this.mvc.perform(get("/api/freeboards/{freeBoardId}", 1L)
                        .header("Authorization", token)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 게시글_하나_조회_데이터_없음() throws Exception {
        this.mvc.perform(get("/api/freeboards/{freeBoardId}", 999L)
                        .header("Authorization", token)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void 게시글_수정_성공() throws Exception {
        FreeBoardRequestDto requestDto = FreeBoardRequestDto.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .build();
        String request = objectMapper.writeValueAsString(requestDto);
        this.mvc.perform(patch("/api/freeboards")
                        .header("Authorization", token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 게시글_수정_실패_권한_없음() throws Exception {
        FreeBoardRequestDto requestDto = FreeBoardRequestDto.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .build();
        String request = objectMapper.writeValueAsString(requestDto);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{Authority.ROLE_USER.toString()})
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        UserDetails principal = new org.springframework.security.core.userdetails.User("2", "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        long now = (new Date()).getTime();
        String accessToken = "Bearer " + tokenProvider.generateAccessToken(authentication, now);

        this.mvc.perform(patch("/api/freeboards")
                        .header("Authorization", accessToken)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 게시글_삭제_성공() throws Exception {
        this.mvc.perform(delete("/api/freeboards/{freeBoardId}", 1L)
                        .header("Authorization", token)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 게시글_삭제_실패_권한_없음() throws Exception {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{Authority.ROLE_USER.toString()})
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        UserDetails principal = new org.springframework.security.core.userdetails.User("2", "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        long now = (new Date()).getTime();
        String accessToken = "Bearer " + tokenProvider.generateAccessToken(authentication, now);

        this.mvc.perform(delete("/api/freeboards/{freeBoardId}", 1L)
                        .header("Authorization", accessToken)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}