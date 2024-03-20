package com.phcworld.answer.infrastructure;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.service.port.FreeBoardAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FreeBoardAnswerRepositoryImpl implements FreeBoardAnswerRepository {

    private final FreeBoardAnswerJpaRepository freeBoardAnswerJpaRepository;

    @Override
    public Optional<FreeBoardAnswer> findById(Long id) {
        return freeBoardAnswerJpaRepository.findById(id).map(FreeBoardAnswerEntity::toModel);
    }

    @Override
    public FreeBoardAnswer save(FreeBoardAnswer freeBoardAnswer) {
        return freeBoardAnswerJpaRepository.save(FreeBoardAnswerEntity.from(freeBoardAnswer)).toModel();
    }
}
