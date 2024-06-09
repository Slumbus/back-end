package com.firefly.slumbus.music.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicDTO {

    private String user;

    private String kid;

    private String music;

    private String title;

    private String picture;

    private String lyric;
}
