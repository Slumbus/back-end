package com.firefly.slumbus.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String email;
    private String password;
    private String picture;
    private String token; // 토큰 추가
}
