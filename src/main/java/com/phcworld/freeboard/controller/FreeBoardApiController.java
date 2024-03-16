package com.phcworld.freeboard.controller;

import com.phcworld.freeboard.controller.port.FreeBoardResponse;
import com.phcworld.freeboard.controller.port.FreeBoardService;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.domain.dto.FreeBoardSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/freeboards")
@RequiredArgsConstructor
public class FreeBoardApiController {

    private final FreeBoardService freeBoardService;

    @PostMapping("")
    public ResponseEntity<FreeBoardResponse> registerBoard(@RequestBody FreeBoardRequest request) {
        FreeBoard freeBoard = freeBoardService.register(request);
        return ResponseEntity
                .status(201)
                .body(FreeBoardResponse.from(freeBoard));
    }

    @GetMapping("")
    public List<FreeBoardResponse> getList(FreeBoardSearch request){
        return freeBoardService.getSearchList(request);
    }

    @GetMapping("/{freeBoardId}")
    public ResponseEntity<FreeBoardResponse> getFreeBoard(@PathVariable(name = "freeBoardId") Long freeBoardId){
        FreeBoard freeBoard = freeBoardService.getFreeBoard(freeBoardId);
        return ResponseEntity
                .status(200)
                .body(FreeBoardResponse.from(freeBoard));
    }

    @PatchMapping("")
    public ResponseEntity<FreeBoardResponse> updateBoard(@RequestBody FreeBoardRequest request) {
        FreeBoard freeBoard = freeBoardService.update(request);
        return ResponseEntity
                .status(200)
                .body(FreeBoardResponse.from(freeBoard));
    }

    @DeleteMapping("/{freeBoardId}")
    public ResponseEntity<FreeBoardResponse> deleteBoard(@PathVariable(name = "freeBoardId") Long freeBoardId){
        FreeBoard freeBoard = freeBoardService.delete(freeBoardId);
        return ResponseEntity
                .status(200)
                .body(FreeBoardResponse.from(freeBoard));
    }
}
