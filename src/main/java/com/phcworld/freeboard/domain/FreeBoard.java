package com.phcworld.freeboard.domain;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.dto.FreeBoardAnswerResponseDto;
import com.phcworld.common.exception.model.DeletedEntityException;
import com.phcworld.common.service.LocalDateTimeHolder;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelect;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FreeBoard {
    private Long id;
    private User writer;
    private String title;
    private String contents;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private int count;
    private boolean isDeleted;
    private boolean isModifyAuthority;
    private boolean isDeleteAuthority;
    private long countOfAnswer;
    private List<FreeBoardAnswer> answers;

    public int getCountOfAnswer() {
        return answers == null ? 0 : answers.size();
    }

    public boolean isNew() {
        final int HOUR_OF_DAY = 24;
        final int MINUTES_OF_HOUR = 60;

        long createdDateAndNowDifferenceMinutes =
                Duration.between(createDate == null ? LocalDateTime.now() : createDate, LocalDateTime.now()).toMinutes();
        return (createdDateAndNowDifferenceMinutes / MINUTES_OF_HOUR) < HOUR_OF_DAY;
    }

    public List<FreeBoardAnswerResponseDto> getAnswers(){
        return answers != null ? answers.stream()
                .map(FreeBoardAnswerResponseDto::of)
                .toList() : new ArrayList<>();
    }

    public boolean matchUser(Long userId) {
        return this.writer.getId().equals(userId);
    }

    public FreeBoard addCount() {
        return FreeBoard.builder()
                .id(id)
                .title(title)
                .contents(contents)
                .count(++count)
                .isDeleted(isDeleted)
                .createDate(createDate)
                .updateDate(updateDate)
                .writer(writer)
                .countOfAnswer(countOfAnswer)
                .answers(answers)
                .isModifyAuthority(isModifyAuthority)
                .isDeleteAuthority(isDeleteAuthority)
                .build();
    }

    public FreeBoard update(String title, String contents) {
        return FreeBoard.builder()
                .id(id)
                .title(title)
                .contents(contents)
                .count(count)
                .isDeleted(isDeleted)
                .createDate(createDate)
                .updateDate(updateDate)
                .writer(writer)
                .countOfAnswer(countOfAnswer)
                .answers(answers)
                .isModifyAuthority(isModifyAuthority)
                .isDeleteAuthority(isDeleteAuthority)
                .build();
    }

    public FreeBoard delete() {
        return FreeBoard.builder()
                .id(id)
                .title(title)
                .contents(contents)
                .count(count)
                .isDeleted(true)
                .createDate(createDate)
                .updateDate(updateDate)
                .writer(writer)
                .countOfAnswer(countOfAnswer)
                .answers(answers)
                .isModifyAuthority(isModifyAuthority)
                .isDeleteAuthority(isDeleteAuthority)
                .build();
    }

    public FreeBoard setAuthorities(Long userId, Authority authorities) {
        if(matchUser(userId)){
            isModifyAuthority = true;
            isDeleteAuthority = true;
        }
        if(authorities == Authority.ROLE_ADMIN){
            isDeleteAuthority = true;
        }
        return FreeBoard.builder()
                .id(id)
                .title(title)
                .contents(contents)
                .count(count)
                .isDeleted(isDeleted)
                .createDate(createDate)
                .updateDate(updateDate)
                .writer(writer)
                .countOfAnswer(countOfAnswer)
                .answers(answers)
                .isModifyAuthority(isModifyAuthority)
                .isDeleteAuthority(isDeleteAuthority)
                .build();
    }

    public static FreeBoard from(FreeBoardRequest request, User writer, LocalDateTimeHolder localDateTimeHolder){
        return FreeBoard.builder()
                .writer(writer)
                .title(request.title())
                .contents(request.contents())
                .createDate(localDateTimeHolder.now())
                .build();
    }

    public static FreeBoard from(FreeBoardSelect freeBoardSelect){
        return FreeBoard.builder()
                .id(freeBoardSelect.getId())
                .writer(freeBoardSelect.getWriter().toModel())
                .title(freeBoardSelect.getTitle())
                .contents(freeBoardSelect.getContents())
                .isDeleted(freeBoardSelect.isDeleted())
                .createDate(freeBoardSelect.getCreateDate())
                .updateDate(freeBoardSelect.getUpdateDate())
                .count(freeBoardSelect.getCount())
                .countOfAnswer(freeBoardSelect.getCountOfAnswer())
                .build();
    }
}
