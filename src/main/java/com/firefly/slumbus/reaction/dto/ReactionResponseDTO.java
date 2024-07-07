package com.firefly.slumbus.reaction.dto;

import com.firefly.slumbus.reaction.entity.Emoji;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactionResponseDTO {
    private Long kidId;
    private Long musicId;
    private Long reactId;
    private Emoji emoji;
    private String comment;
}
