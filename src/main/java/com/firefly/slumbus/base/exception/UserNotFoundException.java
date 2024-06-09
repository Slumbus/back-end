package com.firefly.slumbus.base.exception;

import com.firefly.slumbus.base.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;
}
