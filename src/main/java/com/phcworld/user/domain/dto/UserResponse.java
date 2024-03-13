package com.phcworld.user.domain.dto;

import com.phcworld.user.infrastructure.UserEntity;
import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String email,
        String name,
        String createDate,
        String profileImage
) {
    public static UserResponse of(UserEntity user){
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .createDate(user.getFormattedCreateDate())
//                .profileImage(user.getProfileImageData())
                .profileImage(user.getProfileImageUrl())
                .build();
    }
}
