package com.firefly.slumbus.kid.repository;

import com.firefly.slumbus.kid.entity.KidEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KidRepository extends JpaRepository<KidEntity, Long> {
    List<KidEntity> findByUser_userId(Long userId);
}
