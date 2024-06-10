package com.firefly.slumbus.kid.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class KidRequestDTO {
    private Long userId;
    private String name;
    private Date birth;
    private String picture;
    private Integer gender;
}

