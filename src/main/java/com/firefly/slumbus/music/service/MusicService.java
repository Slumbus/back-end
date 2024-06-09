package com.firefly.slumbus.music.service;

import com.firefly.slumbus.kid.entity.KidEntity;
import com.firefly.slumbus.kid.repository.KidRepository;
import com.firefly.slumbus.music.dto.MusicDTO;
import com.firefly.slumbus.music.entity.MusicEntity;
import com.firefly.slumbus.music.repository.MusicRepository;
import com.firefly.slumbus.user.entity.UserEntity;
import com.firefly.slumbus.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusicService {

//    private final UserRepository userRepository;
//    private final KidRepository kidRepository;
    private final MusicRepository musicRepository;


    public String saveMusic(MusicDTO musicDTO, String userId, String kidId) {

        // 추후 유저 로그인 구현 이후 유저 아이디 받아오면 유저/아이 찾는 로직
//        UserEntity findUser = userRepository.findByUserId(userId);
//        KidEntity findKid = kidRepository.findByKidId(kidId);

        MusicEntity music = MusicEntity.builder()
//                .user(findUser) // 로그인 구현 후 찾아온 유저 넣기
//                .kid(findKid)  // 로그인 구현 후 찾아온 아이 넣기
                .music(musicDTO.getMusic())
                .title(musicDTO.getTitle())
                .picture(musicDTO.getPicture())
                .lyric(musicDTO.getLyric())
                .build();

        musicRepository.save(music);

        return "성공";
    }

}
