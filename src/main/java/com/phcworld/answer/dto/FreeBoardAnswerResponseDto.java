package com.phcworld.answer.dto;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.user.domain.dto.UserResponse;
import lombok.Builder;

@Builder
public record FreeBoardAnswerResponseDto(
        Long id,
        UserResponse writer,
        String contents,
        String updatedDate
) {
    public static FreeBoardAnswerResponseDto of(FreeBoardAnswer freeBoardAnswer) {
        return FreeBoardAnswerResponseDto.builder()
                .id(freeBoardAnswer.getId())
                .writer(UserResponse.from(freeBoardAnswer.getWriter().toModel()))
                .contents(freeBoardAnswer.getContents())
                .updatedDate(freeBoardAnswer.getFormattedUpdateDate())
                .build();
    }
}
