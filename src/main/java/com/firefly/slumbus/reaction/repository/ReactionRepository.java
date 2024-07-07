package com.firefly.slumbus.reaction.repository;

import com.firefly.slumbus.reaction.entity.ReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<ReactionEntity, Long> {
    List<ReactionEntity> findByKid_KidId(Long kidId);
}
