package com.firefly.slumbus.music.repository;

import com.firefly.slumbus.music.entity.MusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<MusicEntity, Long> {
}
