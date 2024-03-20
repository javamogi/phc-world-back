package com.phcworld.answer.service.port;

import com.phcworld.answer.domain.FreeBoardAnswer;

import java.util.Optional;

public interface FreeBoardAnswerRepository {
    Optional<FreeBoardAnswer> findById(Long id);

    FreeBoardAnswer save(FreeBoardAnswer freeBoardAnswer);
}
