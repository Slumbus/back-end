package com.firefly.slumbus.music.service;

import com.firefly.slumbus.kid.entity.KidEntity;
import com.firefly.slumbus.kid.repository.KidRepository;
import com.firefly.slumbus.music.dto.HomeMusicResponseDTO;
import com.firefly.slumbus.music.dto.HomeResponseDTO;
import com.firefly.slumbus.music.dto.MusicRequestDTO;
import com.firefly.slumbus.music.dto.MusicResponseDTO;
import com.firefly.slumbus.music.entity.MusicEntity;
import com.firefly.slumbus.music.repository.MusicRepository;
import com.firefly.slumbus.user.entity.UserEntity;
import com.firefly.slumbus.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.firefly.slumbus.base.UserAuthorizationUtil.getCurrentUserId;

@Service
@RequiredArgsConstructor
public class MusicServiceImpl implements MusicService {

    private final UserRepository userRepository;
    private final KidRepository kidRepository;
    private final MusicRepository musicRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${aiServer.apiUrl}")
    private String apiUrl;


    public MusicResponseDTO saveMusic(MusicRequestDTO musicDTO) {

        Long userId = getCurrentUserId();

        UserEntity findUser = userRepository.findById(userId).get();
        KidEntity findKid = kidRepository.findById(musicDTO.getKidId()).get();

        if (musicDTO.getPicture() == null){
            MusicEntity music = MusicEntity.builder()
                    .user(findUser)
                    .kid(findKid)
                    .music(musicDTO.getMusic())
                    .title(musicDTO.getTitle())
                    .picture("https://slumbus.s3.ap-southeast-2.amazonaws.com/music/slumbus_logo.png")
                    .lyric(musicDTO.getLyric())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            musicRepository.save(music);

            return MusicResponseDTO.builder()
                    .userId(findUser.getUserId())
                    .kidId(findKid.getKidId())
                    .id(music.getMusicId())
                    .url(musicDTO.getMusic())
                    .title(musicDTO.getTitle())
                    .artwork("https://slumbus.s3.ap-southeast-2.amazonaws.com/music/slumbus_logo.png")
                    .lyric(musicDTO.getLyric())
                    .build();
        } else {
            MusicEntity music = MusicEntity.builder()
                    .user(findUser)
                    .kid(findKid)
                    .music(musicDTO.getMusic())
                    .title(musicDTO.getTitle())
                    .picture(musicDTO.getPicture())
                    .lyric(musicDTO.getLyric())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            musicRepository.save(music);

            return MusicResponseDTO.builder()
                    .userId(findUser.getUserId())
                    .kidId(findKid.getKidId())
                    .kidName(findKid.getName())
                    .id(music.getMusicId())
                    .url(musicDTO.getMusic())
                    .title(musicDTO.getTitle())
                    .artwork(musicDTO.getPicture())
                    .lyric(musicDTO.getLyric())
                    .build();
        }
    }

    public MusicResponseDTO getMusicDetails(Long musicId) {
        MusicEntity music = musicRepository.findById(musicId).orElseThrow(() -> new RuntimeException("Music not found"));

        return MusicResponseDTO.builder()
                .userId(music.getUser().getUserId())
                .kidId(music.getKid().getKidId())
                .id(musicId)
                .kidName(music.getKid().getName())
                .url(music.getMusic())
                .title(music.getTitle())
                .artwork(music.getPicture())
                .lyric(music.getLyric())
                .build();
    }

    public List<MusicResponseDTO> getMusicList() {

        Long userId = getCurrentUserId();

        Optional<UserEntity> user = userRepository.findById(userId);
        List<MusicEntity> musicList = musicRepository.findByUser(user.get());

        return musicList.stream()
                .map(music -> MusicResponseDTO.builder()
                        .userId(music.getUser().getUserId())
                        .kidId(music.getKid().getKidId())
                        .id(music.getMusicId())
                        .kidName(music.getKid().getName())
                        .url(music.getMusic())
                        .title(music.getTitle())
                        .artwork(music.getPicture())
                        .lyric(music.getLyric())
                        .build())
                .collect(Collectors.toList());
    }

    public List<MusicResponseDTO> getMusicListByKidId(Long kidId) {

        Optional<KidEntity> kid = kidRepository.findById(kidId);
        List<MusicEntity> musicList = musicRepository.findByKid(kid.get());

        return musicList.stream()
                .map(music -> MusicResponseDTO.builder()
                        .userId(music.getUser().getUserId())
                        .kidId(music.getKid().getKidId())
                        .id(music.getMusicId())
                        .kidName(music.getKid().getName())
                        .url(music.getMusic())
                        .title(music.getTitle())
                        .artwork(music.getPicture())
                        .lyric(music.getLyric())
                        .originMusicId(music.getOriginMusicId())
                        .build())
                .collect(Collectors.toList());
    }

    public MusicResponseDTO updateMusic(Long musicId, MusicRequestDTO musicDTO) {
        MusicEntity music = musicRepository.findById(musicId).orElseThrow(() -> new RuntimeException("Music Not found"));

        if (musicDTO.getPicture() == null){
            music.setTitle(musicDTO.getTitle());
            music.setPicture("https://slumbus.s3.ap-southeast-2.amazonaws.com/music/slumbus_logo.png");

            musicRepository.save(music);

            return MusicResponseDTO.builder()
                    .userId(music.getUser().getUserId())
                    .kidId(music.getKid().getKidId())
                    .id(music.getMusicId())
                    .kidName(music.getKid().getName())
                    .url(music.getMusic())
                    .title(music.getTitle())
                    .artwork("https://slumbus.s3.ap-southeast-2.amazonaws.com/music/slumbus_logo.png")
                    .lyric(music.getLyric())
                    .build();
        } else {
            music.setTitle(musicDTO.getTitle());
            music.setPicture(musicDTO.getPicture());

            musicRepository.save(music);

            return MusicResponseDTO.builder()
                    .userId(music.getUser().getUserId())
                    .kidId(music.getKid().getKidId())
                    .id(music.getMusicId())
                    .kidName(music.getKid().getName())
                    .url(music.getMusic())
                    .title(music.getTitle())
                    .artwork(music.getPicture())
                    .lyric(music.getLyric())
                    .build();
        }
    }

    public void deleteMusic(Long musicId) {

        MusicEntity music = musicRepository.findById(musicId).orElseThrow(() -> new RuntimeException("Music not found"));

        musicRepository.delete(music);
    }

    public MusicResponseDTO updateLyric(Long musicId, String lyric) {

        MusicEntity music = musicRepository.findById(musicId).orElseThrow(() -> new RuntimeException("Music not found"));

        music.setLyric(lyric);
        musicRepository.save(music);

        return MusicResponseDTO.builder()
                .userId(music.getUser().getUserId())
                .kidId(music.getKid().getKidId())
                .id(music.getMusicId())
                .kidName(music.getKid().getName())
                .url(music.getMusic())
                .title(music.getTitle())
                .artwork(music.getPicture())
                .lyric(music.getLyric())
                .build();
    }

    public MusicResponseDTO updateMusicColumn(Long musicId, String musicLink) {

        MusicEntity music = musicRepository.findById(musicId).orElseThrow(() -> new RuntimeException("Music not found"));

        music.setMusic(musicLink);
        music.setOriginMusicId(musicId);
        musicRepository.save(music);

        return MusicResponseDTO.builder()
                .userId(music.getUser().getUserId())
                .kidId(music.getKid().getKidId())
                .kidName(music.getKid().getName())
                .id(music.getMusicId())
                .url(music.getMusic())
                .title(music.getTitle())
                .artwork(music.getPicture())
                .lyric(music.getLyric())
                .originMusicId(music.getOriginMusicId())
                .build();
    }

    @Override
    public List<HomeResponseDTO> getMusicListAll(Long userId) {

        List<KidEntity> kids = kidRepository.findByUser_userId(userId);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return kids.stream().map(kid -> {
            List<HomeMusicResponseDTO> musicList = musicRepository.findByKid(kid).stream()
                    .map(music -> HomeMusicResponseDTO.builder()
                            .musicId(music.getMusicId())
                            .url(music.getMusic())
                            .title(music.getTitle())
                            .artwork(music.getPicture())
                            .lyric(music.getLyric())
                            .build())
                    .collect(Collectors.toList());

            return HomeResponseDTO.builder()
                    .kidId(kid.getKidId())
                    .kidName(kid.getName())
                    .kidPicture(kid.getPicture())
                    .kidBirth(dateFormat.format(kid.getBirth()))
                    .kidGender(kid.getGender())
                    .musicList(musicList)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public String makeMusic(String mood, String instrument, MultipartFile humming) {
        String url = apiUrl + "/compose/music";

        WebClient webClient = webClientBuilder.baseUrl(url).build();

        Mono<String> responseMono = webClient.post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("mood", mood)
                        .with("instrument", instrument)
                        .with("file", humming.getResource()))
                .retrieve()
                .bodyToMono(String.class);

        return responseMono.block();
    }

}
