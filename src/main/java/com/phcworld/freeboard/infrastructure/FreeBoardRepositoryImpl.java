package com.phcworld.freeboard.infrastructure;

import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardSearch;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelect;
import com.phcworld.freeboard.service.port.FreeBoardRepository;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FreeBoardRepositoryImpl implements FreeBoardRepository {

    private final FreeBoardJpaRepository freeBoardJpaRepository;

    @Override
    public Optional<FreeBoard> findById(Long id) {
        return freeBoardJpaRepository.findById(id).map(FreeBoardEntity::toModel);
    }

    @Override
    public FreeBoard save(FreeBoard freeBoard) {
        return freeBoardJpaRepository.save(FreeBoardEntity.from(freeBoard)).toModel();
    }

    @Override
    public List<FreeBoard> findByWriter(User writer) {
        return freeBoardJpaRepository.findByWriter(UserEntity.from(writer))
                .stream()
                .map(FreeBoardEntity::toModel)
                .toList();
    }

    @Override
    public List<FreeBoard> findAllByFetch() {
        return freeBoardJpaRepository.findAllByFetch().stream()
                .map(FreeBoardEntity::toModel)
                .toList();
    }

    @Override
    public List<FreeBoardSelect> findByKeyword(FreeBoardSearch searchDto, Pageable pageable) {
        return freeBoardJpaRepository.findByKeyword(searchDto, pageable);
    }
}
