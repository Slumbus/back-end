package com.firefly.slumbus.music.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HomeResponseDTO {

    private Long kidId;
    private String kidName;
    private String kidPicture;
    private List<HomeMusicResponseDTO> musicList;

}
