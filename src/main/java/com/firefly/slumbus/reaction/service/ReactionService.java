package com.firefly.slumbus.reaction.service;

import com.firefly.slumbus.reaction.dto.ReactionListResponseDTO;
import com.firefly.slumbus.reaction.dto.ReactionRequestDTO;
import com.firefly.slumbus.reaction.dto.ReactionResponseDTO;

import java.util.List;

public interface ReactionService {
    ReactionResponseDTO saveReaction(Long kidId, Long musicId, ReactionRequestDTO reactionRequestDTO);
    List<ReactionListResponseDTO> getReactionList(Long kidId);
    ReactionListResponseDTO getReactionListByMusic(Long kidId, Long musicId);
}
