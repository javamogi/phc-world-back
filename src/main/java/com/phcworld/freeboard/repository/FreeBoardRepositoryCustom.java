package com.phcworld.freeboard.repository;

import com.phcworld.freeboard.dto.FreeBoardSearchDto;
import com.phcworld.freeboard.dto.FreeBoardSelectDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FreeBoardRepositoryCustom {
    List<FreeBoardSelectDto> findByKeyword(FreeBoardSearchDto searchDto, Pageable pageable);
}
