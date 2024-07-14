package com.firefly.slumbus.reaction.dto;

import com.firefly.slumbus.reaction.entity.Emoji;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactionListResponseDTO {
    private Long kidId;
    private Long musicId;
    private String musicTitle;
    private List<ReactionDetailDTO> reactions;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReactionDetailDTO {
        private Long reactId;
        private Emoji emoji;
        private String comment;
        private String createdAt;
    }
}
