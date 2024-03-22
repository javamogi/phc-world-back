package com.phcworld.answer.controller.port;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.common.utils.LocalDateTimeUtils;
import com.phcworld.user.controller.port.UserResponse;
import lombok.Builder;

@Builder
public record FreeBoardAnswerResponse(
        Long id,
        UserResponse writer,
        String contents,
        String updatedDate,
        boolean isDeleted
) {
    public static FreeBoardAnswerResponse from(FreeBoardAnswer freeBoardAnswer) {
        return FreeBoardAnswerResponse.builder()
                .id(freeBoardAnswer.getId())
                .writer(UserResponse.from(freeBoardAnswer.getWriter()))
                .contents(freeBoardAnswer.getContents())
                .updatedDate(LocalDateTimeUtils.getTime(freeBoardAnswer.getUpdateDate()))
                .isDeleted(freeBoardAnswer.isDeleted())
                .build();
    }
}
