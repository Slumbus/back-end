package com.firefly.slumbus.base.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 유저입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
