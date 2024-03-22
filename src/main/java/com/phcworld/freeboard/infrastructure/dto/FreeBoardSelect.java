package com.phcworld.freeboard.infrastructure.dto;

import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.common.utils.LocalDateTimeUtils;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FreeBoardSelect {

    private Long id;
    private UserEntity writer;
    private String title;
    private String contents;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer count;
    private boolean isDeleted;
    private Long countOfAnswer;

}
