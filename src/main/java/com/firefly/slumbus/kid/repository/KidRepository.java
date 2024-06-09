package com.firefly.slumbus.kid.repository;

import com.firefly.slumbus.kid.entity.KidEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KidRepository extends JpaRepository<KidEntity, Long> {
}
