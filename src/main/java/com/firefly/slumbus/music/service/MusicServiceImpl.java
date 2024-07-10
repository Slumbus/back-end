package com.firefly.slumbus.music.service;

import com.firefly.slumbus.kid.entity.KidEntity;
import com.firefly.slumbus.kid.repository.KidRepository;
import com.firefly.slumbus.music.dto.HomeResponseDTO;
import com.firefly.slumbus.music.dto.MusicRequestDTO;
import com.firefly.slumbus.music.dto.MusicResponseDTO;
import com.firefly.slumbus.music.entity.MusicEntity;
import com.firefly.slumbus.music.repository.MusicRepository;
import com.firefly.slumbus.user.entity.UserEntity;
import com.firefly.slumbus.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

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

    public MusicResponseDTO saveMusic(MusicRequestDTO musicDTO) {

        Long userId = getCurrentUserId();

        UserEntity findUser = userRepository.findById(userId).get();
        KidEntity findKid = kidRepository.findById(musicDTO.getKidId()).get();

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
                .music(musicDTO.getMusic())
                .title(musicDTO.getTitle())
                .picture(musicDTO.getPicture())
                .lyric(musicDTO.getLyric())
                .build();
    }

    public MusicResponseDTO getMusicDetails(Long musicId) {
        MusicEntity music = musicRepository.findById(musicId).orElseThrow(() -> new RuntimeException("Music not found"));

        return MusicResponseDTO.builder()
                .userId(music.getUser().getUserId())
                .kidId(music.getKid().getKidId())
                .music(music.getMusic())
                .title(music.getTitle())
                .picture(music.getPicture())
                .lyric(music.getLyric())
                .build();
    }

    public List<MusicResponseDTO> getMusicListByKidId(Long kidId) {

        Optional<KidEntity> kid = kidRepository.findById(kidId);
        List<MusicEntity> musicList = musicRepository.findByKid(kid.get());

        return musicList.stream()
                .map(music -> MusicResponseDTO.builder()
                        .userId(music.getUser().getUserId())
                        .kidId(music.getKid().getKidId())
                        .music(music.getMusic())
                        .title(music.getTitle())
                        .picture(music.getPicture())
                        .lyric(music.getLyric())
                        .build())
                .collect(Collectors.toList());
    }

    public MusicResponseDTO updateMusic(Long musicId, MusicRequestDTO musicDTO) {
        MusicEntity music = musicRepository.findById(musicId).orElseThrow(() -> new RuntimeException("Music Not found"));

        music.setTitle(musicDTO.getTitle());
        music.setPicture(musicDTO.getPicture());

        musicRepository.save(music);

        return MusicResponseDTO.builder()
                .userId(music.getUser().getUserId())
                .kidId(music.getKid().getKidId())
                .music(music.getMusic())
                .title(music.getTitle())
                .picture(music.getPicture())
                .lyric(music.getLyric())
                .build();
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
                .music(music.getMusic())
                .title(music.getTitle())
                .picture(music.getPicture())
                .lyric(music.getLyric())
                .build();
    }

    public MusicResponseDTO updateMusicColumn(Long musicId, String musicLink) {

        MusicEntity music = musicRepository.findById(musicId).orElseThrow(() -> new RuntimeException("Music not found"));

        music.setMusic(musicLink);
        musicRepository.save(music);

        return MusicResponseDTO.builder()
                .userId(music.getUser().getUserId())
                .kidId(music.getKid().getKidId())
                .music(music.getMusic())
                .title(music.getTitle())
                .picture(music.getPicture())
                .lyric(music.getLyric())
                .build();
    }

    @Override
    public List<HomeResponseDTO> getMusicListAll(Long userId) {

        List<KidEntity> kids = kidRepository.findByUser_userId(userId);

        return kids.stream().map(kid -> {
            List<MusicResponseDTO> musicList = musicRepository.findByKid(kid).stream()
                    .map(music -> MusicResponseDTO.builder()
                            .userId(music.getUser().getUserId())
                            .kidId(music.getKid().getKidId())
                            .music(music.getMusic())
                            .title(music.getTitle())
                            .picture(music.getPicture())
                            .lyric(music.getLyric())
                            .build())
                    .collect(Collectors.toList());

            return HomeResponseDTO.builder()
                    .kidId(kid.getKidId())
                    .kidName(kid.getName())
                    .kidPicture(kid.getPicture())
                    .musicList(musicList)
                    .build();
        }).collect(Collectors.toList());
    }
}
