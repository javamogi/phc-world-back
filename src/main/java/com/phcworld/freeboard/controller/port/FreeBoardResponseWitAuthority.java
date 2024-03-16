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
public record FreeBoardResponseWitAuthority(
        Long id,
        UserResponse writer,
        String title,
        String contents,
        String createDate,
        Integer count,
        Integer countOfAnswer,
        Boolean isNew,
        Boolean isModifyAuthority,
        Boolean isDeleteAuthority,
        List<FreeBoardAnswerResponseDto> answers
) {
    public static FreeBoardResponseWitAuthority from(FreeBoard freeBoard){
        return FreeBoardResponseWitAuthority.builder()
                .id(freeBoard.getId())
                .writer(UserResponse.from(freeBoard.getWriter()))
                .title(freeBoard.getTitle())
                .contents(freeBoard.getContents())
                .createDate(LocalDateTimeUtils.getTime(freeBoard.getCreateDate()))
                .count(freeBoard.getCount())
                .countOfAnswer(freeBoard.getCountOfAnswer())
                .isNew(freeBoard.isNew())
                .isModifyAuthority(freeBoard.isModifyAuthority())
                .isDeleteAuthority(freeBoard.isDeleteAuthority())
                .answers(freeBoard.getAnswers())
                .build();
    }
}
