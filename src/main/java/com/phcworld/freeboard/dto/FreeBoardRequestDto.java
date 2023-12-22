package com.phcworld.freeboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record FreeBoardRequestDto(
        Long id,
        @NotBlank
        String title,
        @NotBlank
        String contents
) {
}
