package com.firefly.slumbus.user.service;

import com.firefly.slumbus.user.dto.UserDTO;
import com.firefly.slumbus.user.entity.UserEntity;
import com.firefly.slumbus.user.jwt.JwtProvider;
import com.firefly.slumbus.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Value("${jwt.token-validity-in-seconds}")
    private long jwtExpirationMs;

    public UserService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtProvider = jwtProvider;
    }

    public UserDTO registerUser(String email, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        UserEntity user = UserEntity.builder()
                .email(email)
                .password(encodedPassword)
                .picture(null) // 회원 가입 시에는 프로필 x
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserEntity savedUser = userRepository.save(user);

        String token = jwtProvider.generateJwtToken(savedUser.getUserId(), jwtExpirationMs);

        UserDTO userDto = new UserDTO();
        userDto.setEmail(savedUser.getEmail());
        userDto.setToken(token);

        return userDto;
    }

    public UserDTO loginUser(String email, String rawPassword) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            String token = jwtProvider.generateJwtToken(user.getUserId(), jwtExpirationMs);

            UserDTO userDto = new UserDTO();
            userDto.setEmail(user.getEmail());
            userDto.setToken(token);

            return userDto;
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}
