package com.firefly.slumbus.music.service;

import com.firefly.slumbus.kid.entity.KidEntity;
import com.firefly.slumbus.kid.repository.KidRepository;
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

@Service
@RequiredArgsConstructor
public class MusicServiceImpl implements MusicService {

    private final UserRepository userRepository;
    private final KidRepository kidRepository;
    private final MusicRepository musicRepository;

    public MusicResponseDTO saveMusic(Long userId, MusicRequestDTO musicDTO) {

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
                        .picture(music.getTitle())
                        .picture(music.getPicture())
                        .lyric(music.getLyric())
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteMusic(Long musicId) {

        MusicEntity music = musicRepository.findById(musicId).orElseThrow(() -> new RuntimeException("Music not found"));

        musicRepository.delete(music);
    }
}
