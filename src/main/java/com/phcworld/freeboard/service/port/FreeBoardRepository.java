package com.phcworld.freeboard.service.port;

import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardSearch;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelect;
import com.phcworld.user.domain.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FreeBoardRepository {

    Optional<FreeBoard> findById(Long id);

    FreeBoard save(FreeBoard freeBoard);

    List<FreeBoard> findByWriter(User writer);

    List<FreeBoard> findAllByFetch();

    List<FreeBoardSelect> findByKeyword(FreeBoardSearch searchDto, Pageable pageable);
}
