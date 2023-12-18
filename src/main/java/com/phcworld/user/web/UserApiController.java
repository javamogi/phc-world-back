package com.phcworld.user.web;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.user.dto.LoginUserRequestDto;
import com.phcworld.user.dto.UserRequestDto;
import com.phcworld.user.dto.UserResponseDto;
import com.phcworld.jwt.dto.TokenDto;
import com.phcworld.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;


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

    @GetMapping("/userInfo")
    public UserResponseDto getUserInfo(){
        return userService.getLoginUserInfo();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserInfo(@PathVariable(name = "id") Long id){
        return userService.getUserInfo(id);
    }

    @PatchMapping("")
    public UserResponseDto updateUser(@ModelAttribute UserRequestDto requestDto){
        return userService.modifyUserInfo(requestDto);
    }

    @DeleteMapping("/{id}")
    public SuccessResponseDto deleteUser(@PathVariable(name = "id") Long id){
        return userService.deleteUser(id);
    }
}
