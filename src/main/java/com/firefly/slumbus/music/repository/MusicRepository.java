package com.firefly.slumbus.music.repository;

import com.firefly.slumbus.kid.entity.KidEntity;
import com.firefly.slumbus.music.entity.MusicEntity;
import com.firefly.slumbus.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<MusicEntity, Long> {

    List<MusicEntity> findByKid(KidEntity kid);
    List<MusicEntity> findByUser(UserEntity user);
}
