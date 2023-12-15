package com.phcworld.exception.model;

public class UnauthorizedException extends CustomBaseException{
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public UnauthorizedException(){
        super(ErrorCode.UNAUTHORIZED);
    }
}
