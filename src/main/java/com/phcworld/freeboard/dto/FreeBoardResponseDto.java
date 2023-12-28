package com.phcworld.freeboard.dto;

import com.phcworld.answer.dto.FreeBoardAnswerResponseDto;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.user.dto.UserResponseDto;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record FreeBoardResponseDto(
        Long id,
        UserResponseDto writer,
        String title,
        String contents,
        String createDate,
        Integer count,
        Integer countOfAnswer,
        Boolean isNew,
        List<FreeBoardAnswerResponseDto> answers
) {
    public static FreeBoardResponseDto of(FreeBoard freeBoard){
        return FreeBoardResponseDto.builder()
                .id(freeBoard.getId())
                .writer(UserResponseDto.of(freeBoard.getWriter()))
                .title(freeBoard.getTitle())
                .contents(freeBoard.getContents())
                .createDate(freeBoard.getFormattedCreateDate())
                .count(freeBoard.getCount())
				.countOfAnswer(freeBoard.getCountOfAnswer())
                .isNew(freeBoard.isNew())
                .answers(freeBoard.getFreeBoardAnswers()
                        .stream()
                        .map(FreeBoardAnswerResponseDto::of)
                        .toList())
                .build();
    }
    public static FreeBoardResponseDto of(FreeBoardSelectDto freeBoard){
        return FreeBoardResponseDto.builder()
                .id(freeBoard.getId())
                .writer(UserResponseDto.of(freeBoard.getWriter()))
                .title(freeBoard.getTitle())
                .contents(freeBoard.getContents())
                .createDate(freeBoard.getFormattedCreateDate())
                .count(freeBoard.getCount())
                .countOfAnswer(freeBoard.getCountOfAnswer())
                .isNew(freeBoard.isNew())
                .build();
    }
}
