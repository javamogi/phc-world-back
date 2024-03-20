package com.phcworld.answer.domain;

import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.common.exception.model.DeletedEntityException;
import com.phcworld.common.service.LocalDateTimeHolder;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FreeBoardAnswer {
    private Long id;
    private User writer;
    private FreeBoard freeBoard;
    private String contents;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private boolean isDeleted;

    public static FreeBoardAnswer from(FreeBoardAnswerRequest request, FreeBoard freeBoard, User user, LocalDateTimeHolder localDateTimeHolder) {
        return FreeBoardAnswer.builder()
                .contents(request.contents())
                .freeBoard(freeBoard)
                .writer(user)
                .createDate(localDateTimeHolder.now())
                .isDeleted(false)
                .build();
    }

    public boolean matchWriter(Long userId) {
        return writer.getId().equals(userId);
    }

    public FreeBoardAnswer update(String contents) {
        return FreeBoardAnswer.builder()
                .id(id)
                .writer(writer)
                .freeBoard(freeBoard)
                .contents(contents)
                .createDate(createDate)
                .updateDate(updateDate)
                .isDeleted(isDeleted)
                .build();
    }

    public FreeBoardAnswer delete() {
        if(isDeleted){
            throw new DeletedEntityException();
        }
        return FreeBoardAnswer.builder()
                .id(id)
                .writer(writer)
                .freeBoard(freeBoard)
                .contents(contents)
                .createDate(createDate)
                .updateDate(updateDate)
                .isDeleted(true)
                .build();
    }
}
