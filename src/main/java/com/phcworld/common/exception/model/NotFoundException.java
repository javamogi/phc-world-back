package com.phcworld.common.exception.model;

public class NotFoundException extends CustomBaseException{
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public NotFoundException(){
        super(ErrorCode.NOT_FOUND);
    }
}
