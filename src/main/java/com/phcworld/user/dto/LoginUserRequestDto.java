package com.phcworld.user.dto;

import lombok.*;

@Getter
@Builder
public class LoginUserRequestDto {

	private String email;
	
	private String password;
	
}
