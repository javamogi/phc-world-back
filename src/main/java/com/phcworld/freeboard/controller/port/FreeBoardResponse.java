package com.phcworld.freeboard.controller.port;

import com.phcworld.answer.dto.FreeBoardAnswerResponseDto;
import com.phcworld.common.utils.LocalDateTimeUtils;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelect;
import com.phcworld.user.controller.port.UserResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record FreeBoardResponse(
        Long id,
        UserResponse writer,
        String title,
        String contents,
        String createDate,
        Integer count,
        Integer countOfAnswer,
        Boolean isNew,
        List<FreeBoardAnswerResponseDto> answers
) {
    public static FreeBoardResponse from(FreeBoardEntity freeBoardEntity){
        return FreeBoardResponse.builder()
                .id(freeBoardEntity.getId())
                .writer(UserResponse.from(freeBoardEntity.getWriter().toModel()))
                .title(freeBoardEntity.getTitle())
                .contents(freeBoardEntity.getContents())
                .count(freeBoardEntity.getCount())
                .answers(freeBoardEntity.getFreeBoardAnswers()
                        .stream()
                        .map(FreeBoardAnswerResponseDto::of)
                        .toList())
                .build();
    }
    public static FreeBoardResponse from(FreeBoard freeBoard) {
        return FreeBoardResponse.builder()
                .id(freeBoard.getId())
                .writer(UserResponse.from(freeBoard.getWriter()))
                .title(freeBoard.getTitle())
                .contents(freeBoard.getContents())
                .createDate(LocalDateTimeUtils.getTime(freeBoard.getCreateDate()))
                .count(freeBoard.getCount())
                .countOfAnswer(freeBoard.getCountOfAnswer())
                .isNew(freeBoard.isNew())
                .answers(freeBoard.getAnswers())
                .build();
    }
}
