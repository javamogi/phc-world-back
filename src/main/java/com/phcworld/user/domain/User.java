package com.phcworld.user.domain;

import com.phcworld.user.domain.dto.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private String name;
    private Authority authority;
    private LocalDateTime createDate;
    private String profileImage;
    private boolean isDeleted;

    public static User from(UserRequest requestUser, PasswordEncoder passwordEncoder, LocalDateTime now) {
        return User.builder()
                .email(requestUser.email())
                .name(requestUser.name())
                .password(passwordEncoder.encode(requestUser.password()))
                .authority(Authority.ROLE_USER)
                .createDate(now)
                .profileImage("blank-profile-picture.png")
                .build();
    }

    public User modify(UserRequest requestUser, String profileImg, PasswordEncoder passwordEncoder) {
        return User.builder()
                .id(id)
                .email(email)
                .password(passwordEncoder.encode(requestUser.password()))
                .name(requestUser.name())
                .authority(authority)
                .createDate(createDate)
                .profileImage(profileImg)
                .isDeleted(isDeleted)
                .build();
    }

    public void delete() {
        this.isDeleted = !this.isDeleted;
    }
}
