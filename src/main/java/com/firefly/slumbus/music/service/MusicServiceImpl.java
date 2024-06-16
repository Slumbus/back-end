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

}
