package com.firefly.slumbus.kid.dto;

import com.firefly.slumbus.kid.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KidResponseDTO {
    private Long userId;
    private Long kidId;
    private String name;
    private Date birth;
    private String picture;
    private Gender gender;
}
