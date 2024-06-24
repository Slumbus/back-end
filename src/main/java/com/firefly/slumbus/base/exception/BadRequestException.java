package com.firefly.slumbus.base.exception;

import com.firefly.slumbus.base.code.ErrorCode;

public class BadRequestException extends RuntimeException {

    private final ErrorCode errorCode;

    public BadRequestException() {
        this.errorCode = ErrorCode.BAD_REQUEST;
    }

    public BadRequestException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
