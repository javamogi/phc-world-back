package com.phcworld.user.controller;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.controller.port.UserResponse;
import com.phcworld.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Builder
public class UserApiController {

    private final UserServiceImpl userService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> create(@Valid @ModelAttribute UserRequest userRequest) {
        User user = userService.register(userRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserResponse.from(user));
    }

    @GetMapping("/userInfo")
    public ResponseEntity<UserResponse> getUserInfo(){
        User user = userService.getLoginUserInfo();
        return ResponseEntity
                .ok()
                .body(UserResponse.from(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable(name = "id") Long id){
        User user = userService.getUserInfo(id);
        return ResponseEntity
                .ok()
                .body(UserResponse.from(user));
    }

    @PatchMapping("")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest requestDto){
        User user = userService.modifyUserInfo(requestDto);
        return ResponseEntity
                .ok()
                .body(UserResponse.from(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable(name = "id") Long id){
        User user = userService.delete(id);
        return ResponseEntity
                .ok()
                .body(UserResponse.from(user));
    }

}
