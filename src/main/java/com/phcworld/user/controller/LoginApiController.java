package com.phcworld.user.controller;

import com.phcworld.common.jwt.dto.TokenDto;
import com.phcworld.user.controller.port.LoginService;
import com.phcworld.user.domain.dto.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class LoginApiController {

    private final LoginService loginService;

    @PostMapping("/login")
    public TokenDto login(@Valid @RequestBody LoginRequest user) {
        return loginService.tokenLogin(user);
    }
    @GetMapping("/logout")
    public String logout(){
        return loginService.logout();
    }
    @GetMapping("/newToken")
    public TokenDto getToken(){
        return loginService.getNewToken();
    }
}
