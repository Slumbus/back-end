package com.firefly.slumbus.reaction.repository;

import com.firefly.slumbus.reaction.entity.ReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<ReactionEntity, Long> {
}
