package com.phcworld.answer.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record FreeBoardAnswerRequest(
        Long boardId,
        Long answerId,
        @NotBlank(message = "내용을 입력하세요.")
        String contents
) {
}
