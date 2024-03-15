package com.phcworld.user.controller.port;

import com.phcworld.common.utils.LocalDateTimeUtils;
import com.phcworld.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Builder
public record UserResponse(
        Long id,
        String email,
        String name,
        String createDate,
        String profileImage,
        boolean isDeleted
) {

    public static UserResponse from(User user){
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .createDate(LocalDateTimeUtils.getTime(user.getCreateDate()))
                .profileImage(user.getProfileImage())
                .isDeleted(user.isDeleted())
                .build();
    }
}
