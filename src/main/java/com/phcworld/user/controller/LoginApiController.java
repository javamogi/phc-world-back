package com.phcworld.user.controller;

import com.phcworld.common.jwt.dto.TokenDto;
import com.phcworld.user.controller.port.LoginService;
import com.phcworld.user.domain.dto.LoginRequest;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Builder
public class LoginApiController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginRequest user) {
        TokenDto token = loginService.login(user);
        return ResponseEntity
                .ok()
                .body(token);
    }
    @GetMapping("/newToken")
    public ResponseEntity<TokenDto> getNewToken(){
        TokenDto token = loginService.getNewToken();
        return ResponseEntity
                .ok()
                .body(token);
    }
}
