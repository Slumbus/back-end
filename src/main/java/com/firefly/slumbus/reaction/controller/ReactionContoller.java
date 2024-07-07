package com.firefly.slumbus.reaction.controller;

import com.firefly.slumbus.base.code.ResponseCode;
import com.firefly.slumbus.base.dto.ResponseDTO;
import com.firefly.slumbus.reaction.dto.ReactionRequestDTO;
import com.firefly.slumbus.reaction.dto.ReactionResponseDTO;
import com.firefly.slumbus.reaction.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reaction")
public class ReactionContoller {
    private final ReactionService reactionService;

    @PostMapping("/kid/{kidId}/music/{musicId}")
    public ResponseDTO<ReactionResponseDTO> saveReaction(@PathVariable Long kidId, @PathVariable Long musicId, @RequestBody ReactionRequestDTO reactionRequestDTO) {
        ReactionResponseDTO reaction = reactionService.saveReaction(kidId, musicId, reactionRequestDTO);
        return new ResponseDTO<>(ResponseCode.SUCCESS_SAVE_REACTION, reaction);
    }
}
