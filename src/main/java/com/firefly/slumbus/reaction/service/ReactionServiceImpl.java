package com.firefly.slumbus.reaction.service;

import com.firefly.slumbus.kid.entity.KidEntity;
import com.firefly.slumbus.kid.repository.KidRepository;

import com.firefly.slumbus.music.entity.MusicEntity;
import com.firefly.slumbus.music.repository.MusicRepository;
import com.firefly.slumbus.reaction.dto.ReactionListResponseDTO;
import com.firefly.slumbus.reaction.dto.ReactionRequestDTO;
import com.firefly.slumbus.reaction.dto.ReactionResponseDTO;
import com.firefly.slumbus.reaction.entity.Emoji;
import com.firefly.slumbus.reaction.entity.ReactionEntity;
import com.firefly.slumbus.reaction.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
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
                .musicTitle(savedReaction.getMusic().getTitle())
                .emoji(savedReaction.getEmoji())
                .comment(savedReaction.getComment())
                .build();
    }

    @Override
    public List<ReactionListResponseDTO> getReactionList(Long kidId) {
        List<ReactionEntity> reactions = reactionRepository.findByKid_KidId(kidId);
        Map<Long, List<ReactionEntity>> groupedByMusicId = reactions.stream()
                .collect(Collectors.groupingBy(reaction -> reaction.getMusic().getMusicId()));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return groupedByMusicId.entrySet().stream()
                .map(entry -> {
                    Long musicId = entry.getKey();
                    List<ReactionEntity> reactionEntities = entry.getValue();
                    MusicEntity music = reactionEntities.get(0).getMusic();

                    List<ReactionListResponseDTO.ReactionDetailDTO> reactionDetails = reactionEntities.stream()
                            .map(reaction -> ReactionListResponseDTO.ReactionDetailDTO.builder()
                                    .reactId(reaction.getReactId())
                                    .emoji(reaction.getEmoji())
                                    .comment(reaction.getComment())
                                    .createdAt(reaction.getCreatedAt().format(dateTimeFormatter))
                                    .build())
                            .collect(Collectors.toList());

                    return ReactionListResponseDTO.builder()
                            .kidId(kidId)
                            .musicId(musicId)
                            .musicTitle(music.getTitle())
                            .reactions(reactionDetails)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
