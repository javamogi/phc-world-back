package com.phcworld.answer.service;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.dto.FreeBoardAnswerRequestDto;
import com.phcworld.answer.dto.FreeBoardAnswerResponseDto;
import com.phcworld.answer.repository.FreeBoardAnswerRepository;
import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.NotMatchUserException;
import com.phcworld.common.exception.model.UnauthorizedException;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.repository.FreeBoardRepository;
import com.phcworld.common.security.utils.SecurityUtil;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.user.infrastructure.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class FreeBoardAnswerService {
	
	private final FreeBoardRepository freeBoardRepository;

	private final FreeBoardAnswerRepository freeBoardAnswerRepository;

	private final UserJpaRepository userRepository;
	
	public FreeBoardAnswerResponseDto register(FreeBoardAnswerRequestDto request) {
		FreeBoard freeBoard = freeBoardRepository.findById(request.boardId())
				.orElseThrow(NotFoundException::new);
		Long userId = SecurityUtil.getCurrentMemberId();
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents(request.contents())
				.build();
		
		return FreeBoardAnswerResponseDto.of(freeBoardAnswerRepository.save(freeBoardAnswer));
	}
	
	public FreeBoardAnswerResponseDto getFreeBoardAnswer(Long answerId) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerRepository.findById(answerId)
				.orElseThrow(NotFoundException::new);
		return FreeBoardAnswerResponseDto.of(freeBoardAnswer);
	}
	
	public FreeBoardAnswerResponseDto updateFreeBoardAnswer(FreeBoardAnswerRequestDto request) {
		FreeBoardAnswer answer = freeBoardAnswerRepository.findById(request.answerId())
				.orElseThrow(NotFoundException::new);
		Long userId = SecurityUtil.getCurrentMemberId();

		if(!answer.isSameWriter(userId)){
			throw new NotMatchUserException();
		}

		answer.update(request.contents());
		return FreeBoardAnswerResponseDto.of(answer);
	}
	
	public SuccessResponseDto deleteFreeBoardAnswer(Long answerId) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerRepository.findById(answerId)
				.orElseThrow(NotFoundException::new);
		Long userId = SecurityUtil.getCurrentMemberId();
		Authority authorities = SecurityUtil.getAuthorities();
		if(!freeBoardAnswer.isSameWriter(userId) && authorities != Authority.ROLE_ADMIN) {
			throw new UnauthorizedException();
		}

		freeBoardAnswerRepository.deleteById(answerId);
		
		return SuccessResponseDto.builder()
				.message("삭제성공")
				.statusCode(200)
				.build();
	}
	
}
