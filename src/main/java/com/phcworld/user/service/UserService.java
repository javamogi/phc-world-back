package com.phcworld.user.service;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.common.exception.model.DuplicationException;
import com.phcworld.common.exception.model.NotFoundException;
import com.phcworld.common.exception.model.UnauthorizedException;
import com.phcworld.file.domain.FileType;
import com.phcworld.file.service.UploadFileService;
import com.phcworld.common.jwt.TokenProvider;
import com.phcworld.common.jwt.dto.TokenDto;
import com.phcworld.common.jwt.service.CustomUserDetailsService;
import com.phcworld.common.security.utils.SecurityUtil;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.user.domain.dto.LoginRequest;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.domain.dto.UserResponse;
import com.phcworld.user.infrastructure.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserJpaRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService userDetailsService;
	private final TokenProvider tokenProvider;
	private final UploadFileService uploadFileService;

	public UserEntity registerUser(UserRequest requestUser) {
		boolean isFind = userRepository.findByEmail(requestUser.email())
				.isPresent();
		if(isFind){
			throw new DuplicationException();
		}

		UserEntity user = UserEntity.builder()
				.email(requestUser.email())
				.name(requestUser.name())
				.password(passwordEncoder.encode(requestUser.password()))
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.profileImage("blank-profile-picture.png")
				.build();

		return userRepository.save(user);
	}

	public TokenDto tokenLogin(LoginRequest requestUser) {
		// 비밀번호 확인 + spring security 객체 생성 후 JWT 토큰 생성
		Authentication authentication = SecurityUtil.getAuthentication(requestUser, userDetailsService, passwordEncoder);

		// 토큰 발급
		return tokenProvider.generateTokenDto(authentication);
	}

	public UserResponse getLoginUserInfo(){
		Long userId = SecurityUtil.getCurrentMemberId();
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
		return UserResponse.of(user);
	}

	public UserResponse getUserInfo(Long userId){
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
		return UserResponse.of(user);
	}

	public UserResponse modifyUserInfo(UserRequest requestDto){
        Long userId = SecurityUtil.getCurrentMemberId();
		if(!userId.equals(requestDto.id())){
			throw new UnauthorizedException();
		}
		UserEntity user = userRepository.findById(requestDto.id())
				.orElseThrow(NotFoundException::new);
		String profileImg = user.getProfileImage();
		if(requestDto.imageName() != null){
			profileImg = uploadFileService.registerFile(
					userId,
					requestDto.imageName(),
					requestDto.imageData(),
					FileType.USER_PROFILE_IMG);
		}
		user.modify(passwordEncoder.encode(requestDto.password()), requestDto.name(), profileImg);
		return UserResponse.of(user);
	}

	public SuccessResponseDto deleteUser(Long id) {
		Long userId = SecurityUtil.getCurrentMemberId();
		Authority authorities = SecurityUtil.getAuthorities();

		if(!userId.equals(id) && authorities != Authority.ROLE_ADMIN){
			throw new UnauthorizedException();
		}
		UserEntity user = userRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		user.delete();

		return SuccessResponseDto.builder()
				.statusCode(200)
				.message("삭제 성공")
				.build();
	}

	public String logout() {
		return "로그아웃";
	}

	public TokenDto getNewToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return tokenProvider.generateTokenDto(authentication);
	}
}
