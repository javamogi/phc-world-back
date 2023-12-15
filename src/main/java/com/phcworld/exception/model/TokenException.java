package com.phcworld.exception.model;

import com.phcworld.jwt.dto.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenException extends RuntimeException{
    private TokenDto tokenDto;

}
