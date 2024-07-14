package com.firefly.slumbus.music.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeMusicResponseDTO {

    private Long musicId;

    private String url;

    private String title;

    private String artwork;

    private String lyric;

}
