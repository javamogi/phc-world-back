package com.phcworld.freeboard.infrastructure;

import com.phcworld.freeboard.domain.dto.FreeBoardSearch;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelect;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FreeBoardRepositoryCustom {
    List<FreeBoardSelect> findByKeyword(FreeBoardSearch searchDto, Pageable pageable);
}
