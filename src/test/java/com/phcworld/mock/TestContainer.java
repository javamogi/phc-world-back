package com.phcworld.mock;

import com.phcworld.answer.controller.FreeBoardAnswerApiController;
import com.phcworld.answer.controller.port.FreeBoardAnswerService;
import com.phcworld.answer.service.FreeBoardAnswerServiceImpl;
import com.phcworld.answer.service.port.FreeBoardAnswerRepository;
import com.phcworld.common.jwt.service.CustomUserDetailsService;
import com.phcworld.common.service.LocalDateTimeHolder;
import com.phcworld.freeboard.controller.FreeBoardApiController;
import com.phcworld.freeboard.controller.port.FreeBoardService;
import com.phcworld.freeboard.service.FreeBoardServiceImpl;
import com.phcworld.freeboard.service.port.FreeBoardRepository;
import com.phcworld.user.controller.LoginApiController;
import com.phcworld.user.controller.UserApiController;
import com.phcworld.user.controller.port.LoginService;
import com.phcworld.user.service.LoginServiceImpl;
import com.phcworld.user.service.UserServiceImpl;
import com.phcworld.user.service.port.TokenProvider;
import com.phcworld.user.service.port.UserRepository;
import lombok.Builder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestContainer {
    public final UserApiController userApiController;

    public final LoginService loginService;

    public final LoginApiController loginApiController;

    public final PasswordEncoder passwordEncoder;

    public final UserRepository userRepository;

    public final TokenProvider tokenProvider;

    public final UserDetailsService userDetailsService;

    public final FreeBoardRepository freeBoardRepository;

    public final FreeBoardService freeBoardService;

    public final FreeBoardApiController freeBoardApiController;

    public final FreeBoardAnswerRepository freeBoardAnswerRepository;
    public final FreeBoardAnswerService freeBoardAnswerService;
    public final FreeBoardAnswerApiController freeBoardAnswerApiController;

    @Builder
    public TestContainer(LocalDateTimeHolder localDateTimeHolder){
        this.passwordEncoder = new FakePasswordEncode("test2");
        this.userRepository = new FakeUserRepository();
        this.tokenProvider = new FakeTokenProvider(1, true);
        this.userDetailsService = new CustomUserDetailsService(userRepository);
        UserServiceImpl userService = UserServiceImpl.builder()
                .passwordEncoder(passwordEncoder)
                .userRepository(userRepository)
                .localDateTimeHolder(localDateTimeHolder)
                .build();
        this.userApiController = UserApiController.builder()
                .userService(userService)
                .build();
        this.loginService = LoginServiceImpl.builder()
                .tokenProvider(tokenProvider)
                .passwordEncoder(passwordEncoder)
                .userDetailsService(userDetailsService)
                .build();
        this.loginApiController = LoginApiController.builder()
                .loginService(loginService)
                .build();
        this.freeBoardRepository = new FakeFreeBoardRepository();
        this.freeBoardService = FreeBoardServiceImpl.builder()
                .freeBoardRepository(freeBoardRepository)
                .localDateTimeHolder(localDateTimeHolder)
                .userRepository(userRepository)
                .build();
        this.freeBoardApiController = FreeBoardApiController.builder()
                .freeBoardService(freeBoardService)
                .build();
        this.freeBoardAnswerRepository = new FakeFreeBoardAnswerRepository();
        this.freeBoardAnswerService = FreeBoardAnswerServiceImpl.builder()
                .localDateTimeHolder(localDateTimeHolder)
                .userRepository(userRepository)
                .freeBoardRepository(freeBoardRepository)
                .freeBoardAnswerRepository(freeBoardAnswerRepository)
                .build();
        this.freeBoardAnswerApiController = FreeBoardAnswerApiController.builder()
                .freeBoardAnswerService(freeBoardAnswerService)
                .build();
    }

}
