package com.phcworld.freeboard.web;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.freeboard.dto.FreeBoardRequestDto;
import com.phcworld.freeboard.dto.FreeBoardResponseDto;
import com.phcworld.freeboard.dto.FreeBoardSearchDto;
import com.phcworld.freeboard.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
//    public FreeBoardResponseDto getFreeBoard(@PathVariable(name = "freeBoardId") Long freeBoardId){
    public ResponseEntity<Map<String, Object>> getFreeBoard(@PathVariable(name = "freeBoardId") Long freeBoardId){
//        return freeBoardService.getFreeBoard(freeBoardId);
        Map<String, Object> result = freeBoardService.getFreeBoard(freeBoardId);
        return new ResponseEntity<>(result, HttpStatus.OK);
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
