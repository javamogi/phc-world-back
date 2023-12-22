package com.phcworld.freeboard.service;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.exception.model.DeletedEntityException;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.exception.model.UnauthorizedException;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.dto.*;
import com.phcworld.freeboard.repository.FreeBoardRepository;
import com.phcworld.security.utils.SecurityUtil;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FreeBoardService {
	private final FreeBoardRepository freeBoardRepository;
	private final UserRepository userRepository;

//	private final TimelineServiceImpl timelineService;
	
//	private final AlertServiceImpl alertService;

	@Transactional
	public FreeBoardResponseDto registerFreeBoard(FreeBoardRequestDto request) {
		Long userId = SecurityUtil.getCurrentMemberId();
		User user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
		FreeBoard freeBoard = FreeBoard.builder()
				.writer(user)
				.title(request.title())
				.contents(request.contents())
				.build();

//		timelineService.createTimeline(createdFreeBoard);
		return FreeBoardResponseDto.of(freeBoardRepository.save(freeBoard));
	}

	@Transactional(readOnly = true)
	public List<FreeBoardResponseDto> getSearchList(FreeBoardSearchDto search) {
		PageRequest pageRequest = PageRequest.of(search.pageNum() - 1, search.pageSize(), Sort.by("createDate").descending());
		List<FreeBoardSelectDto> list = freeBoardRepository.findByKeyword(search, pageRequest);
		return list.stream()
				.map(FreeBoardResponseDto::of)
				.toList();
	}

	@Transactional
	public FreeBoardResponseDto getFreeBoard(Long id) {
		FreeBoard freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		if(freeBoard.getIsDeleted()){
			throw new DeletedEntityException();
		}
		freeBoard.addCount();
		return FreeBoardResponseDto.of(freeBoard);
	}

	@Transactional
	public FreeBoardResponseDto updateFreeBoard(FreeBoardRequestDto request) {
		FreeBoard freeBoard = freeBoardRepository.findById(request.id())
				.orElseThrow(NotFoundException::new);
		if(freeBoard.getIsDeleted()){
			throw new DeletedEntityException();
		}
		Long userId = SecurityUtil.getCurrentMemberId();
		Authority authorities = SecurityUtil.getAuthorities();

		if(freeBoard.matchUser(userId) && authorities != Authority.ROLE_ADMIN){
			throw new UnauthorizedException();
		}

		freeBoard.update(request);
		return FreeBoardResponseDto.of(freeBoard);
	}

	@Transactional
	public SuccessResponseDto deleteFreeBoard(Long id) {
		FreeBoard freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		if(freeBoard.getIsDeleted()){
			throw new DeletedEntityException();
		}
		Long userId = SecurityUtil.getCurrentMemberId();
		Authority authorities = SecurityUtil.getAuthorities();

		if(freeBoard.matchUser(userId) && authorities != Authority.ROLE_ADMIN){
			throw new UnauthorizedException();
		}

//		timelineService.deleteTimeline(SaveType.FREE_BOARD, id);
//		alertService.deleteAlert(SaveType.FREE_BOARD, id);
		freeBoard.delete();

		return SuccessResponseDto.builder()
				.statusCode(200)
				.message("삭제 성공")
				.build();
	}

}
