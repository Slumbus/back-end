package com.firefly.slumbus.reaction.entity;

import com.firefly.slumbus.kid.entity.KidEntity;
import com.firefly.slumbus.music.entity.MusicEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactId;

    @ManyToOne
    @JoinColumn(name = "kidId", nullable = false)
    private KidEntity kid;

    @ManyToOne
    @JoinColumn(name = "musicId", nullable = false)
    private MusicEntity music;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Emoji emoji;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
