package com.phcworld.jwt;

import com.phcworld.exception.model.NotFoundException;
import com.phcworld.exception.model.UnauthorizedException;
import com.phcworld.jwt.dto.TokenDto;
import com.phcworld.security.utils.SecurityUtil;
import com.phcworld.user.domain.User;
import com.phcworld.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
class TokenProviderTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    public void 비밀키_암호화(){
        String secretKeyPlain = "spring-security-jwt-phc-world-secret-key";
        // 키를 Base64 인코딩
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        log.info("key : {}", keyBase64Encoded);
        log.info("secret key : {}", secretKey);
        assertThat(keyBase64Encoded).isEqualTo(secretKey);

        byte[] decodeByte = Base64.getDecoder().decode(keyBase64Encoded);
        String str = new String(decodeByte);
        log.info("str : {}", str);
        assertThat(str).isEqualTo(secretKeyPlain);
    }

    @Test
    void 토큰_생성(){
        User user = userRepository.findById(1L)
                .orElseThrow(NotFoundException::new);
        long now = (new Date()).getTime();
        Authentication authentication = SecurityUtil.getAuthentication(user.toAuthentication("test"), userDetailsService, passwordEncoder);
        String accessToken = tokenProvider.generateAccessToken(authentication, now);
        log.info("access token : {}", accessToken);
    }

    @Test
    void 토큰_검증_성공(){
        User user = userRepository.findById(1L)
                .orElseThrow(NotFoundException::new);
        long now = (new Date()).getTime();
        Authentication authentication = SecurityUtil.getAuthentication(user.toAuthentication("test"), userDetailsService, passwordEncoder);
        String accessToken = tokenProvider.generateAccessToken(authentication, now);
        boolean result = tokenProvider.validateToken(accessToken);
        assertThat(result).isTrue();
    }

    @Test
    void 토큰_검증_잘못된_토큰(){
        User user = userRepository.findById(1L)
                .orElseThrow(NotFoundException::new);
        long now = (new Date()).getTime();
        Authentication authentication = SecurityUtil.getAuthentication(user.toAuthentication("test"), userDetailsService, passwordEncoder);
        String accessToken = tokenProvider.generateAccessToken(authentication, now);
        String finalAccessToken = accessToken.replace(".", "");
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            tokenProvider.validateToken(finalAccessToken);
        });
    }

    @Test
    void 토큰_검증_만료된_토큰(){
        String accessToken = Jwts.builder()
                .setExpiration(new Date(new Date().getTime()))
                .compact();

        Assertions.assertThrows(UnauthorizedException.class, () -> {
            tokenProvider.validateToken(accessToken);
        });
    }

    @Test
    void 토큰_응답_dto_생성(){
        User user = userRepository.findById(1L)
                .orElseThrow(NotFoundException::new);
        Authentication authentication = SecurityUtil.getAuthentication(user.toAuthentication("test"), userDetailsService, passwordEncoder);
        TokenDto dto = tokenProvider.generateTokenDto(authentication);
        log.info("token : {}", dto);
    }

    @Test
    void encodePassword(){
        String password = passwordEncoder.encode("test");
        log.info("password : {}", password);
        assertThat(passwordEncoder.matches("test", password)).isTrue();
    }

}