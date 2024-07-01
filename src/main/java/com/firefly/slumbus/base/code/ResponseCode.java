package com.firefly.slumbus.base.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS_LOGIN(HttpStatus.OK, "로그인을 성공했습니다."),
    SUCCESS_REGISTER(HttpStatus.OK, "회원가입을 성공했습니다."),
    SUCCESS_SEND_CODE(HttpStatus.OK, "인증 번호를 전송했습니다."),
    SUCCESS_CHECK_CODE(HttpStatus.OK, "인증을 성공했습니다."),
    SUCCESS_REGISTER_KID(HttpStatus.OK, "아이 등록에 성공했습니다."),
    SUCCESS_GET_KID_LIST(HttpStatus.OK, "아이 목록 조회에 성공했습니다."),
    SUCCESS_GET_KID_DETAIL(HttpStatus.OK, "아이 상세 조회에 성공했습니다."),
    SUCCESS_UPDATE_KID(HttpStatus.OK, "아이 수정을 성공했습니다."),
    SUCCESS_DELETE_KID(HttpStatus.OK, "아이 삭제를 성공했습니다."),
    SUCCESS_SAVE_MUSIC(HttpStatus.OK, "자장가 저장에 성공했습니다."),
    SUCCESS_GET_MUSIC_DETAIL(HttpStatus.OK, "자장가 상세 조회에 성공했습니다."),
    SUCCESS_GET_MUSIC_LIST(HttpStatus.OK, "자장가 목록 조회에 성공했습니다."),
    SUCCESS_UPDATE_MUSIC(HttpStatus.OK, "자장가 수정에 성공했습니다."),
    SUCCESS_DELETE_MUSIC(HttpStatus.OK, "자장가 삭제를 성공했습니다."),
    SUCCESS_PUT_LYRIC(HttpStatus.OK, "작사 저장에 성공했습니다."),
    SUCCESS_SAVE_COMPLETE_MUSIC(HttpStatus.OK, "최종 자장가 음악 저장을 성공했습니다."),
    SUCCESS_GET_MY_PAGE(HttpStatus.OK, "마이페이지 조회에 성공했습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
