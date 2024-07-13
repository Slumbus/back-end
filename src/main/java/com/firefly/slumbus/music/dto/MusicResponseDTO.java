package com.firefly.slumbus.music.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicResponseDTO {

    private Long userId;

    private Long kidId;

    private Long id;

    private String url;

    private String title;

    private String artwork;

    private String lyric;
}
