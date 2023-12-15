package com.phcworld.user.dto;

import com.phcworld.user.domain.User;
import lombok.Builder;

@Builder
public record UserResponseDto(
        Long id,
        String email,
        String name,
        String creatDate,
        String profileImage
) {
    public static UserResponseDto of(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .creatDate(user.getFormattedCreateDate())
                .profileImage(user.getProfileImage())
                .build();
    }
}
