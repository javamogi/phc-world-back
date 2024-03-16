package com.phcworld.freeboard.domain;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.dto.FreeBoardAnswerResponseDto;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
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
    private List<FreeBoardAnswer> answers;

    public static FreeBoard from(FreeBoardRequest request, User writer){
        return FreeBoard.builder()
                .writer(writer)
                .title(request.title())
                .contents(request.contents())
                .build();
    }

    public int getCountOfAnswer() {
        return answers.size();
    }

    public boolean isNew() {
        final int HOUR_OF_DAY = 24;
        final int MINUTES_OF_HOUR = 60;

        long createdDateAndNowDifferenceMinutes =
                Duration.between(createDate == null ? LocalDateTime.now() : createDate, LocalDateTime.now()).toMinutes();
        return (createdDateAndNowDifferenceMinutes / MINUTES_OF_HOUR) < HOUR_OF_DAY;
    }

    public List<FreeBoardAnswerResponseDto> getAnswers(){
        return answers.stream()
                .map(FreeBoardAnswerResponseDto::of)
                .toList();
    }

    public boolean matchUser(Long userId) {
        return !this.writer.getId().equals(userId);
    }

    public void addCount() {
        this.count += 1;
    }

    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void setAuthority(Long userId, Authority authorities) {
        if(!matchUser(userId)){
            isModifyAuthority = true;
            isDeleteAuthority = true;
        }
        if(authorities == Authority.ROLE_ADMIN){
            isDeleteAuthority = true;
        }
    }
}
