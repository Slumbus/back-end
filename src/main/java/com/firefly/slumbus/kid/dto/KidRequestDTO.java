package com.firefly.slumbus.kid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KidRequestDTO {
    private Long userId;
    private String name;
    private Date birth;
    private String picture;
    private Integer gender;
}

