package com.phcworld.exception.model;

public class DeletedUserException extends CustomBaseException{

    public DeletedUserException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public DeletedUserException() {
        super(ErrorCode.CONFLICT);
    }
}
