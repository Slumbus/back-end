package com.firefly.slumbus.user.repository;

import com.firefly.slumbus.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email); // 이메일로 사용자 정보를 가져옴
}
