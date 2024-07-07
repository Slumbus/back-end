package com.firefly.slumbus.reaction.service;

import com.firefly.slumbus.reaction.dto.ReactionRequestDTO;
import com.firefly.slumbus.reaction.dto.ReactionResponseDTO;

public interface ReactionService {
    ReactionResponseDTO saveReaction(Long kidId, Long musicId, ReactionRequestDTO reactionRequestDTO);
}
