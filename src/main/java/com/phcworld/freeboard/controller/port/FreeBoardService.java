package com.phcworld.freeboard.controller.port;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.domain.dto.FreeBoardSearch;

import java.util.List;
import java.util.Map;

public interface FreeBoardService {
    FreeBoard register(FreeBoardRequest request);
    List<FreeBoardResponse> getSearchList(FreeBoardSearch search);
    FreeBoard getFreeBoard(Long id);
    FreeBoard update(FreeBoardRequest request);
    FreeBoard delete(Long id);
}
