package com.phcworld.user.service;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.common.exception.model.DuplicationException;
import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.UnauthorizedException;
import com.phcworld.common.security.utils.SecurityUtil;
import com.phcworld.common.service.LocalDateTimeHolder;
import com.phcworld.user.controller.port.UserService;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.controller.port.UserResponse;
import com.phcworld.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Builder
public class UserServiceImpl implements UserService {

	private final PasswordEncoder passwordEncoder;
//	private final UploadFileService uploadFileService;
	private final UserRepository userRepository;
	private final LocalDateTimeHolder localDateTimeHolder;

	@Override
	public User register(UserRequest requestUser) {
		userRepository.findByEmail(requestUser.email())
				.ifPresent(
						user -> {
							throw new DuplicationException();
						}
				);

		User user = User.from(requestUser, passwordEncoder, localDateTimeHolder);
		return userRepository.save(user);
	}

	@Override
	public User getLoginUserInfo(){
		Long userId = SecurityUtil.getCurrentMemberId();
		return userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
	}

	@Override
	public User getUserInfo(Long userId){
		return userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
	}

	@Override
	public User modifyUserInfo(UserRequest requestDto){
        Long userId = SecurityUtil.getCurrentMemberId();
		if(!userId.equals(requestDto.id())){
			throw new UnauthorizedException();
		}
		User user = userRepository.findById(requestDto.id())
				.orElseThrow(NotFoundException::new);
		String profileImg = user.getProfileImage();
//		if(requestDto.imageName() != null){
//			profileImg = uploadFileService.registerFile(
//					userId,
//					requestDto.imageName(),
//					requestDto.imageData(),
//					FileType.USER_PROFILE_IMG);
//		}
		user = user.modify(requestDto, profileImg, passwordEncoder);
		return userRepository.save(user);
	}

	@Override
	public User delete(Long id) {
		Long userId = SecurityUtil.getCurrentMemberId();
		Authority authorities = SecurityUtil.getAuthorities();

		if(!userId.equals(id) && authorities != Authority.ROLE_ADMIN){
			throw new UnauthorizedException();
		}
		User user = userRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		user.delete();
		return userRepository.save(user);
	}

}
