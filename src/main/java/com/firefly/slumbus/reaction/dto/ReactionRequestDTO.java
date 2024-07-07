package com.firefly.slumbus.reaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactionRequestDTO {
    private Integer emoji;
    private String comment;
}
