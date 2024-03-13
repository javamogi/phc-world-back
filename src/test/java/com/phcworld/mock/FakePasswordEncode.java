package com.phcworld.mock;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class FakePasswordEncode implements PasswordEncoder {

    private final String password;

    @Override
    public String encode(CharSequence rawPassword) {
        return password;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }
}
