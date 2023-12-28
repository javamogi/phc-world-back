package com.phcworld.answer.web;

import com.phcworld.answer.dto.FreeBoardAnswerRequestDto;
import com.phcworld.answer.dto.FreeBoardAnswerResponseDto;
import com.phcworld.answer.service.FreeBoardAnswerService;
import com.phcworld.common.dto.SuccessResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/freeboards/answers")
@RequiredArgsConstructor
public class FreeBoardAnswerApiController {
	
	private final FreeBoardAnswerService freeBoardAnswerService;
	
	@PostMapping("")
	@ResponseStatus(value = HttpStatus.CREATED)
	public FreeBoardAnswerResponseDto register(@RequestBody FreeBoardAnswerRequestDto request) {
		return freeBoardAnswerService.register(request);
	}
	
	@GetMapping("/{id}")
	public FreeBoardAnswerResponseDto getFreeBoardAnswer(@PathVariable(name = "id") Long id) {
		return freeBoardAnswerService.getFreeBoardAnswer(id);
	}
	
	@PatchMapping("")
	public FreeBoardAnswerResponseDto updateFreeBoardAnswer(@RequestBody FreeBoardAnswerRequestDto request) {
		return freeBoardAnswerService.updateFreeBoardAnswer(request);
	}
	
	@DeleteMapping("/{id}")
	public SuccessResponseDto deleteFreeBoardAnswer(@PathVariable(name = "id") Long id) {
		return freeBoardAnswerService.deleteFreeBoardAnswer(id);
	}

}
