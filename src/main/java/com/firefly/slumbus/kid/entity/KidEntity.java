package com.firefly.slumbus.kid.entity;

import com.firefly.slumbus.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class KidEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kidId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Date birth;

    @Column(columnDefinition = "TEXT")
    private String picture;

    @Column(nullable = true)
    private Boolean gender;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
