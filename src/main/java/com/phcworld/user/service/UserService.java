package com.phcworld.user.service;

import com.phcworld.common.dto.SuccessResponseDto;
import com.phcworld.exception.model.DuplicationException;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.exception.model.UnauthorizedException;
import com.phcworld.jwt.TokenProvider;
import com.phcworld.jwt.dto.TokenDto;
import com.phcworld.jwt.service.CustomUserDetailsService;
import com.phcworld.security.utils.SecurityUtil;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.dto.LoginUserRequestDto;
import com.phcworld.user.dto.UserRequestDto;
import com.phcworld.user.dto.UserResponseDto;
import com.phcworld.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService userDetailsService;
	private final TokenProvider tokenProvider;

	public User registerUser(UserRequestDto requestUser) {
		boolean isFind = userRepository.findByEmail(requestUser.email())
				.isPresent();
		if(isFind){
			throw new DuplicationException();
		}

		User user = User.builder()
				.email(requestUser.email())
				.name(requestUser.name())
				.password(passwordEncoder.encode(requestUser.password()))
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.profileImage("blank-profile-picture.png")
				.build();

		return userRepository.save(user);
	}

	public TokenDto tokenLogin(LoginUserRequestDto requestUser) {
		// 비밀번호 확인 + spring security 객체 생성 후 JWT 토큰 생성
		Authentication authentication = SecurityUtil.getAuthentication(requestUser, userDetailsService, passwordEncoder);

		// 토큰 발급
		return tokenProvider.generateTokenDto(authentication);
	}

	public UserResponseDto getLoginUserInfo(){
		Long userId = SecurityUtil.getCurrentMemberId();
		User user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
		return UserResponseDto.of(user);
	}

	public UserResponseDto getUserInfo(Long userId){
		User user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
		return UserResponseDto.of(user);
	}

	public UserResponseDto modifyUserInfo(UserRequestDto requestDto){
		Long userId = SecurityUtil.getCurrentMemberId();
		if(!userId.equals(requestDto.id())){
			throw new UnauthorizedException();
		}
		User user = userRepository.findById(requestDto.id())
				.orElseThrow(NotFoundException::new);
		user.modify(requestDto);
		return UserResponseDto.of(user);
	}

	public SuccessResponseDto deleteUser(Long id) {
		Long userId = SecurityUtil.getCurrentMemberId();
		String authorities = SecurityUtil.getAuthorities();

		if(!userId.equals(id) && !authorities.equals(Authority.ROLE_ADMIN.toString())){
			throw new UnauthorizedException();
		}
		User user = userRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		user.delete();

		return SuccessResponseDto.builder()
				.statusCode(200)
				.message("삭제 성공")
				.build();
	}
}
