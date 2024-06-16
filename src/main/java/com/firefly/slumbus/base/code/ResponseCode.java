package com.firefly.slumbus.base.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS_LOGIN(HttpStatus.OK, "로그인을 성공했습니다."),
    SUCCESS_REGISTER(HttpStatus.OK, "회원가입을 성공했습니다."),
    SUCCESS_REGISTER_KID(HttpStatus.OK, "아이 등록에 성공했습니다."),
    SUCCESS_GET_KIDLIST(HttpStatus.OK, "아이 목록 조회에 성공했습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
