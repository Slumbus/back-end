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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/song")
public class MusicController {

    private final MusicService musicService;

    // 작곡 후 저장
    @PostMapping("/composition")
    public ResponseDTO<MusicResponseDTO> saveMusic(@RequestBody MusicRequestDTO musicRequestDTO) {

        try {

            MusicResponseDTO savedMusic = musicService.saveMusic(musicRequestDTO);

            return new ResponseDTO<>(ResponseCode.SUCCESS_SAVE_MUSIC, savedMusic);
        } catch (Exception e) {
            String message = e.getMessage();
            return new ResponseDTO<>(0, "실패", message, null);
        }
    }

    // 자장가 상세 조회(+가사 조회)
    @GetMapping("/detail/{musicId}")
    public ResponseDTO<MusicResponseDTO> getMusicDetails(@PathVariable("musicId") Long musicId) {

        MusicResponseDTO musicDetails = musicService.getMusicDetails(musicId);

        return new ResponseDTO<>(ResponseCode.SUCCESS_GET_MUSIC_DETAIL, musicDetails);
    }

    // 자장가 목록 조회
    @GetMapping("/list/{kidId}")
    public ResponseDTO<List<MusicResponseDTO>> getMusicListByKidId(@PathVariable("kidId") Long kidId) {

        List<MusicResponseDTO> musicList = musicService.getMusicListByKidId(kidId);

        return new ResponseDTO<>(ResponseCode.SUCCESS_GET_MUSIC_LIST, musicList);
    }

    // 자장가 수정(제목, 앨범자켓)
    @PutMapping("/{musicId}")
    public ResponseDTO<MusicResponseDTO> updateMusic(@PathVariable("musicId") Long musicId, @RequestBody MusicRequestDTO musicRequestDTO) {

        MusicResponseDTO updatedMusic = musicService.updateMusic(musicId, musicRequestDTO);

        return new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_MUSIC, updatedMusic);
    }

    // 자장가 삭제
    @DeleteMapping("/{musicId}")
    public ResponseDTO<Long> deleteMusic(@PathVariable("musicId") Long musicId) {

        musicService.deleteMusic(musicId);

        return new ResponseDTO<>(ResponseCode.SUCCESS_DELETE_MUSIC, musicId);
    }

    // 작사 후 저장
    @PutMapping("/lyric/{musicId}")
    public ResponseDTO<MusicResponseDTO> putLyric(@PathVariable("musicId") Long musicId, @RequestParam("lyric") String lyric) {

        MusicResponseDTO updatedMusic = musicService.updateLyric(musicId, lyric);

        return new ResponseDTO<>(ResponseCode.SUCCESS_PUT_LYRIC, updatedMusic);
    }

    // 자장가 최종 음악(가사 녹음 합본) 저장
    @PatchMapping("/update/{musicId}")
    public ResponseDTO<MusicResponseDTO> updateMusicColumn(@PathVariable("musicId") Long musicId, @RequestParam("music") String music) {

        MusicResponseDTO updatedMusic = musicService.updateMusicColumn(musicId, music);

        return new ResponseDTO<>(ResponseCode.SUCCESS_SAVE_COMPLETE_MUSIC, updatedMusic);
    }
}
