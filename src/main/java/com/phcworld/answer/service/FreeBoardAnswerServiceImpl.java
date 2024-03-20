package com.phcworld.answer.service;

import com.phcworld.answer.controller.port.FreeBoardAnswerService;
import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.answer.service.port.FreeBoardAnswerRepository;
import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.NotMatchUserException;
import com.phcworld.common.exception.model.UnauthorizedException;
import com.phcworld.common.security.utils.SecurityUtil;
import com.phcworld.common.service.LocalDateTimeHolder;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.service.port.FreeBoardRepository;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class FreeBoardAnswerServiceImpl implements FreeBoardAnswerService {
	
	private final FreeBoardRepository freeBoardRepository;

	private final FreeBoardAnswerRepository freeBoardAnswerRepository;

	private final UserRepository userRepository;

	private final LocalDateTimeHolder localDateTimeHolder;

	@Override
	public FreeBoardAnswer register(FreeBoardAnswerRequest request) {
		FreeBoard freeBoard = freeBoardRepository.findById(request.boardId())
				.orElseThrow(NotFoundException::new);
		Long userId = SecurityUtil.getCurrentMemberId();
		User user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.from(request, freeBoard, user, localDateTimeHolder);
		
		return freeBoardAnswerRepository.save(freeBoardAnswer);
	}

	@Override
	public FreeBoardAnswer getFreeBoardAnswer(Long answerId) {
		return freeBoardAnswerRepository.findById(answerId)
				.orElseThrow(NotFoundException::new);
	}

	@Override
	public FreeBoardAnswer update(FreeBoardAnswerRequest request) {
		FreeBoardAnswer answer = freeBoardAnswerRepository.findById(request.answerId())
				.orElseThrow(NotFoundException::new);
		Long userId = SecurityUtil.getCurrentMemberId();

		if(!answer.matchWriter(userId)){
			throw new NotMatchUserException();
		}

		answer = answer.update(request.contents());
		return freeBoardAnswerRepository.save(answer);
	}

	@Override
	public FreeBoardAnswer delete(Long answerId) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerRepository.findById(answerId)
				.orElseThrow(NotFoundException::new);
		Long userId = SecurityUtil.getCurrentMemberId();
		Authority authorities = SecurityUtil.getAuthorities();
		if(!freeBoardAnswer.matchWriter(userId) && authorities != Authority.ROLE_ADMIN) {
			throw new UnauthorizedException();
		}

		freeBoardAnswer = freeBoardAnswer.delete();

		return freeBoardAnswerRepository.save(freeBoardAnswer);
	}
	
}
