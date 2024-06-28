package com.firefly.slumbus.base.exception;

import com.firefly.slumbus.base.code.ErrorCode;
import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {

    private final ErrorCode errorCode;

    public ConflictException() {
        this.errorCode = ErrorCode.CONFLICT;
    }

    public ConflictException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
