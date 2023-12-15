package com.phcworld.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequestDto {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호를을 입력하세요.")
    @Size(min = 4, message = "비밀번호는 4자 이상으로 해야합니다.")
    private String password;

    @NotBlank(message = "이름을 입력하세요.")
    @Size(min = 3, max = 20, message = "이름은 영문 3자 이상 20자 이하 또는 한글 두자이상 6자 이하로 해야합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "이름은 한글, 영문, 숫자만 가능합니다.")
    private String name;
}
