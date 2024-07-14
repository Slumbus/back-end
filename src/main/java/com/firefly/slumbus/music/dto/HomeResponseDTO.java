package com.firefly.slumbus.music.dto;

import com.firefly.slumbus.kid.entity.Gender;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HomeResponseDTO {

    private Long kidId;
    private String kidName;
    private String kidPicture;
    private String kidBirth;
    private Gender kidGender;
    private List<HomeMusicResponseDTO> musicList;

}
