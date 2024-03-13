package com.phcworld.user.service;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.common.exception.model.DuplicationException;
import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.UnauthorizedException;
import com.phcworld.file.domain.FileType;
import com.phcworld.file.service.UploadFileService;
import com.phcworld.common.security.utils.SecurityUtil;
import com.phcworld.user.controller.port.UserService;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.domain.dto.UserResponse;
import com.phcworld.user.infrastructure.UserJpaRepository;
import com.phcworld.user.service.port.UserRepository;
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
public class UserServiceImpl implements UserService {

	private final PasswordEncoder passwordEncoder;
	private final UploadFileService uploadFileService;
	private final UserRepository userRepository;

	@Override
	public UserResponse register(UserRequest requestUser) {
		userRepository.findByEmail(requestUser.email())
				.ifPresent(
						user -> {
							throw new DuplicationException();
						}
				);

		User user = User.from(requestUser, passwordEncoder, LocalDateTime.now());
		return UserResponse.from(userRepository.save(user));
	}

	@Override
	public UserResponse getLoginUserInfo(){
		Long userId = SecurityUtil.getCurrentMemberId();
		User user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
		return UserResponse.from(user);
	}

	@Override
	public UserResponse getUserInfo(Long userId){
		User user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
		return UserResponse.from(user);
	}

	@Override
	public UserResponse modifyUserInfo(UserRequest requestDto){
        Long userId = SecurityUtil.getCurrentMemberId();
		if(!userId.equals(requestDto.id())){
			throw new UnauthorizedException();
		}
		User user = userRepository.findById(requestDto.id())
				.orElseThrow(NotFoundException::new);
		String profileImg = user.getProfileImage();
		if(requestDto.imageName() != null){
			profileImg = uploadFileService.registerFile(
					userId,
					requestDto.imageName(),
					requestDto.imageData(),
					FileType.USER_PROFILE_IMG);
		}
		user = user.modify(requestDto, profileImg, passwordEncoder);
		return UserResponse.from(userRepository.save(user));
	}

	@Override
	public SuccessResponseDto delete(Long id) {
		Long userId = SecurityUtil.getCurrentMemberId();
		Authority authorities = SecurityUtil.getAuthorities();

		if(!userId.equals(id) && authorities != Authority.ROLE_ADMIN){
			throw new UnauthorizedException();
		}
		User user = userRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		user.delete();
		userRepository.save(user);
		return SuccessResponseDto.builder()
				.statusCode(200)
				.message("삭제 성공")
				.build();
	}

}
