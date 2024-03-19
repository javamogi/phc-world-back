package com.phcworld.freeboard.service;

import com.phcworld.common.exception.model.DeletedEntityException;
import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.UnauthorizedException;
import com.phcworld.common.security.utils.SecurityUtil;
import com.phcworld.common.service.LocalDateTimeHolder;
import com.phcworld.freeboard.controller.port.FreeBoardService;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.domain.dto.FreeBoardSearch;
import com.phcworld.freeboard.service.port.FreeBoardRepository;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder
public class FreeBoardServiceImpl implements FreeBoardService {
	private final FreeBoardRepository freeBoardRepository;
	private final UserRepository userRepository;
//	private final UploadFileService uploadFileService;
	private final LocalDateTimeHolder localDateTimeHolder;

	@Override
	@Transactional
	public FreeBoard register(FreeBoardRequest request) {
		Long userId = SecurityUtil.getCurrentMemberId();
		User user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);

//		String contents = uploadFileService.registerImages(request.contents());

		FreeBoard freeBoard = FreeBoard.from(request, user, localDateTimeHolder);

		return freeBoardRepository.save(freeBoard);
	}

	@Override
	@Transactional(readOnly = true)
	public List<FreeBoard> getSearchList(FreeBoardSearch search) {
		PageRequest pageRequest = PageRequest.of(search.pageNum() - 1, search.pageSize(), Sort.by("createDate").descending());
		return freeBoardRepository.findByKeyword(search, pageRequest);
	}

	@Override
	@Transactional
	public FreeBoard getFreeBoard(Long id) {
		FreeBoard freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		if(freeBoard.isDeleted()){
			throw new DeletedEntityException();
		}

		Long userId = SecurityUtil.getCurrentMemberId();
		Authority authorities = SecurityUtil.getAuthorities();
		freeBoard = freeBoard.setAuthorities(userId, authorities);
		freeBoard = freeBoard.addCount();
		return freeBoardRepository.save(freeBoard);
	}

	@Override
	@Transactional
	public FreeBoard update(FreeBoardRequest request) {
		FreeBoard freeBoard = freeBoardRepository.findById(request.id())
				.orElseThrow(NotFoundException::new);
		if(freeBoard.isDeleted()){
			throw new DeletedEntityException();
		}

		Long userId = SecurityUtil.getCurrentMemberId();
		Authority authorities = SecurityUtil.getAuthorities();
		if(!freeBoard.matchUser(userId) && authorities != Authority.ROLE_ADMIN){
			throw new UnauthorizedException();
		}

//		String contents = uploadFileService.registerImages(request.contents());

		freeBoard = freeBoard.update(request.title(), request.contents());
		return freeBoardRepository.save(freeBoard);
	}

	@Override
	@Transactional
	public FreeBoard delete(Long id) {
		FreeBoard freeBoard = freeBoardRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		if(freeBoard.isDeleted()){
			throw new DeletedEntityException();
		}

		Long userId = SecurityUtil.getCurrentMemberId();
		Authority authorities = SecurityUtil.getAuthorities();
		if(!freeBoard.matchUser(userId) && authorities != Authority.ROLE_ADMIN){
			throw new UnauthorizedException();
		}

		freeBoard = freeBoard.delete();

		return freeBoardRepository.save(freeBoard);
	}

}
