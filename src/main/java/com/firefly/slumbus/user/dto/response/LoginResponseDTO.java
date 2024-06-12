package com.firefly.slumbus.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {

    private String email;
    private String token;
}
