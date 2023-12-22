package com.phcworld.freeboard.web;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.freeboard.dto.FreeBoardRequestDto;
import com.phcworld.freeboard.dto.FreeBoardResponseDto;
import com.phcworld.freeboard.dto.FreeBoardSearchDto;
import com.phcworld.freeboard.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/freeboards")
@RequiredArgsConstructor
public class FreeBoardApiController {

    private final FreeBoardService freeBoardService;

    @PostMapping("")
    public FreeBoardResponseDto registerBoard(@RequestBody FreeBoardRequestDto request) {
        return freeBoardService.registerFreeBoard(request);
    }

    @GetMapping("")
    public List<FreeBoardResponseDto> getList(FreeBoardSearchDto request){
        return freeBoardService.getSearchList(request);
    }

    @GetMapping("/{freeBoardId}")
    public FreeBoardResponseDto getFreeBoard(@PathVariable(name = "freeBoardId") Long freeBoardId){
        return freeBoardService.getFreeBoard(freeBoardId);
    }

    @PatchMapping("")
    public FreeBoardResponseDto updateBoard(@RequestBody FreeBoardRequestDto request) {
        return freeBoardService.updateFreeBoard(request);
    }

    @DeleteMapping("/{freeBoardId}")
    public SuccessResponseDto deleteBoard(@PathVariable(name = "freeBoardId") Long freeBoardId){
        return freeBoardService.deleteFreeBoard(freeBoardId);
    }
}
