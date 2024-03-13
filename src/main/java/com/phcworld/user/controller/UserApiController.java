package com.phcworld.user.controller;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.domain.dto.UserResponse;
import com.phcworld.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserServiceImpl userService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @ModelAttribute UserRequest user) {
        return userService.register(user);
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
        return userService.delete(id);
    }


}
