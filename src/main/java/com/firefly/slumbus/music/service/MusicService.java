package com.firefly.slumbus.music.service;

import com.firefly.slumbus.music.dto.MusicRequestDTO;
import com.firefly.slumbus.music.dto.MusicResponseDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface MusicService {

    MusicResponseDTO saveMusic(Long userId, MusicRequestDTO musicDTO);
}
