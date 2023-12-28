package com.phcworld.answer.dto;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.user.domain.User;
import com.phcworld.user.dto.UserResponseDto;
import lombok.Builder;

@Builder
public record FreeBoardAnswerResponseDto(
        Long id,
        UserResponseDto writer,
        String contents,
        String countOfAnswers,
        String updatedDate
) {
    public static FreeBoardAnswerResponseDto of(FreeBoardAnswer freeBoardAnswer) {
        return FreeBoardAnswerResponseDto.builder()
                .id(freeBoardAnswer.getId())
                .writer(UserResponseDto.of(freeBoardAnswer.getWriter()))
                .contents(freeBoardAnswer.getContents())
                .updatedDate(freeBoardAnswer.getFormattedUpdateDate())
                .build();
    }
}
