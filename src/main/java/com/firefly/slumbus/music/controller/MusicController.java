package com.firefly.slumbus.music.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firefly.slumbus.base.code.ResponseCode;
import com.firefly.slumbus.base.config.S3Service;
import com.firefly.slumbus.base.dto.ResponseDTO;
import com.firefly.slumbus.music.dto.*;
import com.firefly.slumbus.music.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.firefly.slumbus.base.UserAuthorizationUtil.getCurrentUserId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/song")
public class MusicController {

    private static final String FLASK_API_URL = "http://localhost:5000/lyrics/write";
    private final ObjectMapper objectMapper;

    private final MusicService musicService;
    private final S3Service s3Service;

    // 작곡 후 저장
    @PostMapping("/composition")
    public ResponseDTO<MusicResponseDTO> saveMusic(@RequestParam("musicDTO") String musicDTOString,
//                                                   @RequestPart("musicFile") MultipartFile musicFile,
                                                   @RequestParam(value = "image", required = false) MultipartFile albumImage) {

        try {

            // 음악 파일 s3에 업로드
//            String musicURL = s3Service.uploadMusic(musicFile);
//            musicRequestDTO.setMusic(musicURL);
            ObjectMapper mapper = new ObjectMapper();
            MusicRequestDTO musicRequestDTO = mapper.readValue(musicDTOString, MusicRequestDTO.class);

            String imageURL = null;
            if (albumImage != null && !albumImage.isEmpty()) {
                imageURL = s3Service.uploadImage(albumImage);
            }
            musicRequestDTO.setPicture(imageURL);
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
    public ResponseDTO<MusicResponseDTO> updateMusic(@PathVariable("musicId") Long musicId,
                                                     @RequestPart("musicDTO") MusicRequestDTO musicRequestDTO,
                                                     @RequestPart("image") MultipartFile albumImage) {

        String imageURL = s3Service.uploadImage(albumImage);
        musicRequestDTO.setPicture(imageURL);
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
    public ResponseDTO<MusicResponseDTO> updateMusicColumn(@PathVariable("musicId") Long musicId, @RequestPart("musicFile") MultipartFile musicFile) {

        String musicURL = s3Service.uploadMusic(musicFile);

        MusicResponseDTO updatedMusic = musicService.updateMusicColumn(musicId, musicURL);

        return new ResponseDTO<>(ResponseCode.SUCCESS_SAVE_COMPLETE_MUSIC, updatedMusic);
    }

    // chatGPT 통해 가사 생성
    @PostMapping("/genLyrics")
    public ResponseEntity<String> generateLyrics(@RequestBody LyricsRequestDTO input) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAcceptCharset(List.of(StandardCharsets.UTF_8));

            String requestBody = objectMapper.writeValueAsString(input);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            ResponseEntity<String> response = restTemplate.postForEntity(FLASK_API_URL, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new String(response.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new String(response.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 홈화면 - 유저별 자장가 목록 조회(모두)
    @GetMapping("/home")
    public ResponseDTO<List<HomeResponseDTO>> getMusicListAll() {

        Long userId = getCurrentUserId();

        List<HomeResponseDTO> musicList = musicService.getMusicListAll(userId);

        return new ResponseDTO<>(ResponseCode.SUCCESS_GET_MUSIC_LIST, musicList);
    }

    //생성형 AI로 자장가 생성
    @PostMapping("/compose")
    public ResponseEntity<ResponseDTO> writeSong(@RequestParam("options") String musicOptions, @RequestParam("humming") MultipartFile humming) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        MusicOptionsDTO musicOptionsDTO = mapper.readValue(musicOptions, MusicOptionsDTO.class);

        String music = musicService.makeMusic(musicOptionsDTO.getMood(), musicOptionsDTO.getInstrument(), humming);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_COMPOSE_MUSIC.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_COMPOSE_MUSIC, music));
    }

}
