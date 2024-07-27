package com.firefly.slumbus.kid.entity;

import com.firefly.slumbus.music.entity.MusicEntity;
import com.firefly.slumbus.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "kid")
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

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private Gender gender;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "kid", cascade = CascadeType.REMOVE)
    private List<MusicEntity> musicList;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
