package com.firefly.slumbus.music.service;

import com.firefly.slumbus.music.dto.HomeResponseDTO;
import com.firefly.slumbus.music.dto.MusicRequestDTO;
import com.firefly.slumbus.music.dto.MusicResponseDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface MusicService {

    MusicResponseDTO saveMusic(MusicRequestDTO musicDTO);
    MusicResponseDTO getMusicDetails(Long musicId);
    List<MusicResponseDTO> getMusicListByKidId(Long kidId);
    MusicResponseDTO updateMusic(Long musicId, MusicRequestDTO musicDTO);
    void deleteMusic(Long musicId);
    MusicResponseDTO updateLyric(Long musicId, String lyric);
    MusicResponseDTO updateMusicColumn(Long musicId, String Music);
    List<HomeResponseDTO> getMusicListAll(Long userId);
}
