package com.firefly.slumbus.music.controller;

import com.firefly.slumbus.base.code.ResponseCode;
import com.firefly.slumbus.base.dto.ResponseDTO;
import com.firefly.slumbus.music.dto.MusicRequestDTO;
import com.firefly.slumbus.music.dto.MusicResponseDTO;
import com.firefly.slumbus.music.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/song")
public class MusicController {

    private final MusicService musicService;

    @PostMapping("/composition")
    public ResponseDTO<MusicResponseDTO> saveMusic(@RequestParam("userId") Long userId, @RequestBody MusicRequestDTO musicRequestDTO) {

        try {

            MusicResponseDTO savedMusic = musicService.saveMusic(userId, musicRequestDTO);

            return new ResponseDTO<>(ResponseCode.SUCCESS_SAVE_MUSIC, savedMusic);
        } catch (Exception e) {
            String message = e.getMessage();
            return new ResponseDTO<>(0, "실패", message, null);
        }
    }

}
