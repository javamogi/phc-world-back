package com.phcworld.user.controller;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.controller.port.UserResponse;
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
        return UserResponse.from(userService.register(user));
    }

    @GetMapping("/userInfo")
    public UserResponse getUserInfo(){
        return UserResponse.from(userService.getLoginUserInfo());
    }

    @GetMapping("/{id}")
    public UserResponse getUserInfo(@PathVariable(name = "id") Long id){
        return UserResponse.from(userService.getUserInfo(id));
    }

    @PatchMapping("")
    public UserResponse updateUser(@RequestBody UserRequest requestDto){
        return UserResponse.from(userService.modifyUserInfo(requestDto));
    }

    @DeleteMapping("/{id}")
    public UserResponse deleteUser(@PathVariable(name = "id") Long id){
        return UserResponse.from(userService.delete(id));
    }

}
