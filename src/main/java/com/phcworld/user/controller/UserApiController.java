package com.phcworld.user.controller;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.user.domain.dto.LoginRequest;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.domain.dto.UserResponse;
import com.phcworld.common.jwt.dto.TokenDto;
import com.phcworld.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserService userService;

    @PostMapping("")
    public UserResponse create(@Valid @ModelAttribute UserRequest user) {
        return UserResponse.of(userService.registerUser(user));
    }

    @PostMapping("/login")
    public TokenDto login(@Valid @RequestBody LoginRequest user) {
        return userService.tokenLogin(user);
    }

    @GetMapping("/userInfo")
    public UserResponse getUserInfo(){
        return userService.getLoginUserInfo();
    }

    @GetMapping("/{id}")
    public UserResponse getUserInfo(@PathVariable(name = "id") Long id){
        return userService.getUserInfo(id);
    }

    @PatchMapping("")
    public UserResponse updateUser(@RequestBody UserRequest requestDto){
        return userService.modifyUserInfo(requestDto);
    }

    @DeleteMapping("/{id}")
    public SuccessResponseDto deleteUser(@PathVariable(name = "id") Long id){
        return userService.deleteUser(id);
    }

    @GetMapping("/logout")
    public String logout(){
        return userService.logout();
    }

    @GetMapping("/newToken")
    public TokenDto getToken(){
        return userService.getNewToken();
    }
}
