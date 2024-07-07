package com.firefly.slumbus.reaction.service;

import com.firefly.slumbus.kid.entity.KidEntity;
import com.firefly.slumbus.kid.repository.KidRepository;

import com.firefly.slumbus.music.entity.MusicEntity;
import com.firefly.slumbus.music.repository.MusicRepository;
import com.firefly.slumbus.reaction.dto.ReactionRequestDTO;
import com.firefly.slumbus.reaction.dto.ReactionResponseDTO;
import com.firefly.slumbus.reaction.entity.Emoji;
import com.firefly.slumbus.reaction.entity.ReactionEntity;
import com.firefly.slumbus.reaction.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final KidRepository kidRepository;
    private final MusicRepository musicRepository;

    @Override
    public ReactionResponseDTO saveReaction(Long kidId, Long musicId, ReactionRequestDTO reactionRequestDTO) {
        KidEntity kid = kidRepository.findById(kidId)
                .orElseThrow(() -> new RuntimeException("Kid not found"));
        MusicEntity music = musicRepository.findById(musicId)
                .orElseThrow(() -> new RuntimeException("Music not found"));

        Emoji emoji = null;
        switch (reactionRequestDTO.getEmoji()){
            case 0:
                emoji = Emoji.DEEPSLEEP;
                break;
            case 1:
                emoji = Emoji.SLEEP;
                break;
            case 2:
                emoji = Emoji.GOOD;
                break;
            case 3:
                emoji = Emoji.BAD;
                break;
            case 4:
                emoji = Emoji.SAD;
                break;
            case 5:
                emoji = Emoji.MAD;
                break;
        }

        ReactionEntity reactionEntity = ReactionEntity.builder()
                .kid(kid)
                .music(music)
                .emoji(emoji)
                .comment(reactionRequestDTO.getComment())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ReactionEntity savedReaction = reactionRepository.save(reactionEntity);

        return ReactionResponseDTO.builder()
                .reactId(savedReaction.getReactId())
                .kidId(savedReaction.getKid().getKidId())
                .musicId(savedReaction.getMusic().getMusicId())
                .emoji(savedReaction.getEmoji())
                .comment(savedReaction.getComment())
                .build();
    }

    @Override
    public List<ReactionResponseDTO> getReactionList(Long kidId) {
        return reactionRepository.findByKid_KidId(kidId).stream()
                .map(reaction -> ReactionResponseDTO.builder()
                        .reactId(reaction.getReactId())
                        .kidId(reaction.getKid().getKidId())
                        .musicId(reaction.getMusic().getMusicId())
                        .emoji(reaction.getEmoji())
                        .comment(reaction.getComment())
                        .build())
                .collect(Collectors.toList());
    }
}
