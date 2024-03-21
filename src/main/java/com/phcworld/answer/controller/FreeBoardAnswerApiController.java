package com.phcworld.answer.controller;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.answer.controller.port.FreeBoardAnswerResponse;
import com.phcworld.answer.service.FreeBoardAnswerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/freeboards/answers")
@RequiredArgsConstructor
public class FreeBoardAnswerApiController {
	
	private final FreeBoardAnswerServiceImpl freeBoardAnswerService;
	
	@PostMapping("")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<FreeBoardAnswerResponse> register(@RequestBody FreeBoardAnswerRequest request) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerService.register(request);
		return ResponseEntity
				.status(201)
				.body(FreeBoardAnswerResponse.from(freeBoardAnswer));
	}
	
//	@GetMapping("/{id}")
//	public ResponseEntity<FreeBoardAnswerResponse> getFreeBoardAnswer(@PathVariable(name = "id") Long id) {
//		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerService.getFreeBoardAnswer(id);
//		return ResponseEntity
//				.status(200)
//				.body(FreeBoardAnswerResponse.from(freeBoardAnswer));
//	}
	
	@PatchMapping("")
	public ResponseEntity<FreeBoardAnswerResponse> update(@RequestBody FreeBoardAnswerRequest request) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerService.update(request);
		return ResponseEntity
				.status(200)
				.body(FreeBoardAnswerResponse.from(freeBoardAnswer));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<FreeBoardAnswerResponse> delete(@PathVariable(name = "id") Long id) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerService.delete(id);
		return ResponseEntity
				.status(200)
				.body(FreeBoardAnswerResponse.from(freeBoardAnswer));
	}

}
