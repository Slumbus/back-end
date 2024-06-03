package com.firefly.slumbus.noise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NoiseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noiseId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String noise;
}
