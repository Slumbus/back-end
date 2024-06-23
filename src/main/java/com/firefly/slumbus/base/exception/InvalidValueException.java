package com.firefly.slumbus.base.exception;

import com.firefly.slumbus.base.code.ErrorCode;

public class InvalidValueException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidValueException() {
        this.errorCode = ErrorCode.BAD_REQUEST;
    }

    public InvalidValueException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
