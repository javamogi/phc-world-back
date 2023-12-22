package com.phcworld.freeboard.dto;

import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.user.dto.UserResponseDto;
import lombok.Builder;

@Builder
public record FreeBoardResponseDto(
        Long id,
        UserResponseDto writer,
        String title,
        String contents,
        String createDate,
        Integer count,
//        String countOfAnswer,
        Boolean isNew
) {
    public static FreeBoardResponseDto of(FreeBoard freeBoard){
        return FreeBoardResponseDto.builder()
                .id(freeBoard.getId())
                .writer(UserResponseDto.of(freeBoard.getWriter()))
                .title(freeBoard.getTitle())
                .contents(freeBoard.getContents())
                .createDate(freeBoard.getFormattedCreateDate())
                .count(freeBoard.getCount())
//				.countOfAnswer(freeBoard.getCountOfAnswer())
                .isNew(freeBoard.isNew())
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
//                .countOfAnswer(freeBoard.countOfAnswer().toString())
                .isNew(freeBoard.isNew())
                .build();
    }
}
