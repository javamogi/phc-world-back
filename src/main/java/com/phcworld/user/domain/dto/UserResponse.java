package com.phcworld.user.domain.dto;

import com.phcworld.common.utils.LocalDateTimeUtils;
import com.phcworld.user.domain.User;
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

    public static UserResponse from(User user){
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .createDate(LocalDateTimeUtils.getTime(user.getCreateDate()))
                .profileImage(user.getProfileImage())
                .build();
    }
}
