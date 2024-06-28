package com.firefly.slumbus.base.exception;

import com.firefly.slumbus.base.code.ErrorCode;
import lombok.Getter;

@Getter
public class InternalServerException extends RuntimeException {

    private final ErrorCode errorCode;

    public InternalServerException() {
        this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    public InternalServerException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
