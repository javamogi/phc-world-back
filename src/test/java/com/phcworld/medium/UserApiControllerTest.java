package com.phcworld.medium;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phcworld.common.jwt.TokenProviderImpl;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.dto.LoginRequest;
import com.phcworld.user.domain.dto.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(SpringExtension.class)
class UserApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenProviderImpl tokenProvider;

    @Test
    void 회원가입_성공() throws Exception {
        this.mvc.perform(post("/users")
                        .with(csrf())
                        .param("email", "abcdefg@test.test")
                        .param("password", "abcde")
                        .param("name", "에이비씨디이"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void 회원가입_실패_중복_이메일() throws Exception {
        this.mvc.perform(post("/users")
                        .with(csrf())
                        .param("email", "test@test.test")
                        .param("password", "abcde")
                        .param("name", "에이비씨디이"))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void 회원가입_실패_모든_요소_빈값() throws Exception {
        this.mvc.perform(post("/users")
                        .with(csrf())
                        .param("email", "")
                        .param("password", "")
                        .param("name", ""))
                .andDo(print())
                .andExpect(jsonPath("$.messages.length()").value(6))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원가입_실패_이메일_입력_없음() throws Exception {
        this.mvc.perform(post("/users")
                        .with(csrf())
                        .param("email", "")
                        .param("password", "password")
                        .param("name", "name"))
                .andDo(print())
                .andExpect(jsonPath("$.messages.length()").value(1))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원가입_실패_이메일_형식_아님() throws Exception {
        this.mvc.perform(post("/users")
                        .with(csrf())
                        .param("email", "test")
                        .param("password", "password")
                        .param("name", "name"))
                .andDo(print())
                .andExpect(jsonPath("$.messages.[0]").value("이메일 형식이 아닙니다."))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원가입_실패_비밀번호_입력_없음() throws Exception {
        this.mvc.perform(post("/users")
                        .with(csrf())
                        .param("email", "testttt@test.test")
                        .param("password", "")
                        .param("name", "name"))
                .andDo(print())
                .andExpect(jsonPath("$.messages.length()").value(2))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원가입_실패_비밀번호_입력_최소입력_미만() throws Exception {
        this.mvc.perform(post("/users")
                        .with(csrf())
                        .param("email", "testttt@test.test")
                        .param("password", "tes")
                        .param("name", "name"))
                .andDo(print())
                .andExpect(jsonPath("$.messages.[0]").value("비밀번호는 4자 이상으로 해야합니다."))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원가입_실패_이름_입력_없음() throws Exception {
        this.mvc.perform(post("/users")
                        .with(csrf())
                        .param("email", "testttt@test.test")
                        .param("password", "password")
                        .param("name", ""))
                .andDo(print())
                .andExpect(jsonPath("$.messages.length()").value(3))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원가입_실패_이름_특수문자_입력() throws Exception {
        this.mvc.perform(post("/users")
                        .with(csrf())
                        .param("email", "testttt@test.test")
                        .param("password", "password")
                        .param("name", "!@#$$"))
                .andDo(print())
                .andExpect(jsonPath("$.messages.[0]").value("이름은 한글, 영문, 숫자만 가능합니다."))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원가입_실패_이름_최소입력_미만() throws Exception {
        this.mvc.perform(post("/users")
                        .with(csrf())
                        .param("email", "testttt@test.test")
                        .param("password", "password")
                        .param("name", "ab"))
                .andDo(print())
                .andExpect(jsonPath("$.messages.[0]").value("이름은 영문 3자 이상 20자 이하 또는 한글 두자이상 6자 이하로 해야합니다."))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원가입_실패_이름_최대입력_초과() throws Exception {
        this.mvc.perform(post("/users")
                        .with(csrf())
                        .param("email", "testttt@test.test")
                        .param("password", "password")
                        .param("name", "aaaaabbbbbcccccddddde"))
                .andDo(print())
                .andExpect(jsonPath("$.messages.[0]").value("이름은 영문 3자 이상 20자 이하 또는 한글 두자이상 6자 이하로 해야합니다."))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원_로그인_실패_비밀번호_틀림() throws Exception {
        LoginRequest requestDto = LoginRequest.builder()
                .email("test@test.test")
                .password("testt")
                .build();
        String request = objectMapper.writeValueAsString(requestDto);
        this.mvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(jsonPath("$.error").value("잘못된 요청입니다."))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원_로그인_실패_없는_이메일() throws Exception {
        LoginRequest requestDto = LoginRequest.builder()
                .email("testtest@test.test")
                .password("test")
                .build();
        String request = objectMapper.writeValueAsString(requestDto);
        this.mvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(jsonPath("$.error").value("존재하지 않는 엔티티입니다."))
                .andExpect(status().isNotFound());
    }

    @Test
    void 회원_로그인_실패_삭제된_회원() throws Exception {
        LoginRequest requestDto = LoginRequest.builder()
                .email("test3@test.test")
                .password("test3")
                .build();
        String request = objectMapper.writeValueAsString(requestDto);
        this.mvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void 로그인_성공() throws Exception {
        LoginRequest requestDto = LoginRequest.builder()
                .email("test@test.test")
                .password("test")
                .build();
        String request = objectMapper.writeValueAsString(requestDto);

        this.mvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 로그인_회원_정보_가져오기() throws Exception {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{Authority.ROLE_ADMIN.toString()})
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        UserDetails principal = new org.springframework.security.core.userdetails.User("1", "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        long now = (new Date()).getTime();
        String accessToken = tokenProvider.generateAccessToken(authentication, now);

        this.mvc.perform(get("/users/userInfo")
                        .with(csrf())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@test.test"))
                .andExpect(jsonPath("$.name").value("테스트"))
                .andExpect(status().isOk());
    }

    @Test
    void 요청_회원_정보_가져오기() throws Exception {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{Authority.ROLE_ADMIN.toString()})
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        UserDetails principal = new org.springframework.security.core.userdetails.User("1", "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        long now = (new Date()).getTime();
        String accessToken = tokenProvider.generateAccessToken(authentication, now);

        this.mvc.perform(get("/users/{id}", 2L)
                        .with(csrf())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("test2@test.test"))
                .andExpect(jsonPath("$.name").value("테스트2"))
                .andExpect(status().isOk());
    }

    @Test
    void 회원_정보_변경() throws Exception {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{Authority.ROLE_ADMIN.toString()})
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        UserDetails principal = new org.springframework.security.core.userdetails.User("1", "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        long now = (new Date()).getTime();
        String accessToken = tokenProvider.generateAccessToken(authentication, now);

        File file = new File("src/main/resources/static/image/PHC-WORLD.png");
        byte[] bytesFile = Files.readAllBytes(file.toPath());
        String imgData = Base64.getEncoder().encodeToString(bytesFile);

        UserRequest userRequestDto = UserRequest.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .imageName("test.png")
                .imageData(imgData)
                .build();

        String request = objectMapper.writeValueAsString(userRequestDto);

        this.mvc.perform(patch("/users")
                        .with(csrf())
                        .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                        )
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@test.test"))
                .andExpect(jsonPath("$.name").value("테스트"))
                .andExpect(status().isOk());
    }

    @Test
    void 회원_정보_변경_실패_요청_회원_다름() throws Exception {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{Authority.ROLE_ADMIN.toString()})
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        UserDetails principal = new org.springframework.security.core.userdetails.User("1", "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        long now = (new Date()).getTime();
        String accessToken = tokenProvider.generateAccessToken(authentication, now);

        UserRequest requestDto = UserRequest.builder()
                .id(2L)
                .email("test2@test.test")
                .password("test2")
                .name("test2")
                .build();
        String request = objectMapper.writeValueAsString(requestDto);

        this.mvc.perform(patch("/users")
                        .with(csrf())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 회원_정보_삭제_성공() throws Exception {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{Authority.ROLE_ADMIN.toString()})
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        UserDetails principal = new org.springframework.security.core.userdetails.User("1", "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        long now = (new Date()).getTime();
        String accessToken = tokenProvider.generateAccessToken(authentication, now);

        this.mvc.perform(delete("/users/{id}", 1L)
                        .with(csrf())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(jsonPath("$.isDeleted").value(true))
                .andExpect(status().isOk());
    }

    @Test
    void 회원_정보_삭제_성공_관리자_권한() throws Exception {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{Authority.ROLE_ADMIN.toString()})
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        UserDetails principal = new org.springframework.security.core.userdetails.User("1", "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        long now = (new Date()).getTime();
        String accessToken = tokenProvider.generateAccessToken(authentication, now);

        this.mvc.perform(delete("/users/{id}", 2L)
                        .with(csrf())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 회원_정보_삭제_요청_회원_다름() throws Exception {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{Authority.ROLE_USER.toString()})
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        UserDetails principal = new org.springframework.security.core.userdetails.User("2", "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        long now = (new Date()).getTime();
        String accessToken = tokenProvider.generateAccessToken(authentication, now);

        this.mvc.perform(delete("/users/{id}", 1L)
                        .with(csrf())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 새로운_토큰_발행() throws Exception {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{Authority.ROLE_USER.toString()})
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        UserDetails principal = new org.springframework.security.core.userdetails.User("2", "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        long now = (new Date()).getTime();
        String refreshToken = tokenProvider.generateRefreshToken(authentication, now);

        this.mvc.perform(get("/users/newToken")
                        .with(csrf())
                        .header("Authorization", "Bearer " + refreshToken))
                .andDo(print())
                .andExpect(status().isOk());
    }

}