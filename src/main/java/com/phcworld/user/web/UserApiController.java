package com.phcworld.user.web;

import com.phcworld.user.dto.LoginUserRequestDto;
import com.phcworld.user.dto.UserRequestDto;
import com.phcworld.user.dto.UserResponseDto;
import com.phcworld.jwt.dto.TokenDto;
import com.phcworld.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserService userService;

    @PostMapping("")
    public UserResponseDto create(@Valid @ModelAttribute UserRequestDto user) {
        return UserResponseDto.of(userService.registerUser(user));
    }

    @PostMapping("/login")
    public TokenDto login(@Valid @RequestBody LoginUserRequestDto user) {
        return userService.tokenLogin(user);
    }
}
