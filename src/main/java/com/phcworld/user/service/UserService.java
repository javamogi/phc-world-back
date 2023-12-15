package com.phcworld.user.service;

import com.phcworld.exception.model.DuplicationException;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.jwt.TokenProvider;
import com.phcworld.jwt.dto.TokenDto;
import com.phcworld.jwt.service.CustomUserDetailsService;
import com.phcworld.security.utils.SecurityUtil;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.dto.LoginUserRequestDto;
import com.phcworld.user.dto.UserRequestDto;
import com.phcworld.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
		boolean isFind = userRepository.findByEmail(requestUser.getEmail())
				.isPresent();
		if(isFind){
			throw new DuplicationException();
		}

		User user = User.builder()
				.email(requestUser.getEmail())
				.name(requestUser.getName())
				.password(passwordEncoder.encode(requestUser.getPassword()))
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.profileImage("blank-profile-picture.png")
				.build();

		return userRepository.save(user);
	}

	public TokenDto tokenLogin(LoginUserRequestDto requestUser) {
		String email = requestUser.getEmail();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException());

		// 비밀번호 확인 + spring security 객체 생성 후 JWT 토큰 생성
		UsernamePasswordAuthenticationToken authenticationToken = user.toAuthentication(requestUser.getPassword());
		Authentication authentication = SecurityUtil.getAuthentication(authenticationToken, userDetailsService, passwordEncoder);

		// 토큰 발급
		return tokenProvider.generateTokenDto(authentication);
	}

}
