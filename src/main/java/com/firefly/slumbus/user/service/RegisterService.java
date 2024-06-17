package com.firefly.slumbus.user.service;

import com.firefly.slumbus.user.dto.request.UserRequestDTO;
import com.firefly.slumbus.user.entity.UserEntity;
import com.firefly.slumbus.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * 회원 가입
     */
    public Long registerUser(UserRequestDTO request) {

        validateDuplicateMember(request);

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .picture(null) // 회원 가입 시에는 프로필 x
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return user.getUserId();
    }

    private void validateDuplicateMember(UserRequestDTO request) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(request.getEmail());
        // 이미 존재하는 이메일이면 가입 불가
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 가입된 유저입니다.");
        }
    }
}
