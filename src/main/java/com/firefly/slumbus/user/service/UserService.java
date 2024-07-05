package com.firefly.slumbus.user.service;

import com.firefly.slumbus.user.dto.response.LoginResponseDTO;
import com.firefly.slumbus.user.dto.request.UserRequestDTO;
import com.firefly.slumbus.user.entity.UserEntity;
import com.firefly.slumbus.user.jwt.JwtProvider;
import com.firefly.slumbus.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public LoginResponseDTO loginUser(UserRequestDTO request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtProvider.generateJwtToken(user.getUserId(), jwtExpirationMs);

            LoginResponseDTO userDto = new LoginResponseDTO();
            userDto.setEmail(user.getEmail());
            userDto.setToken(token);

            return userDto;
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public boolean patchPassword(UserRequestDTO request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        user.updatePassword(passwordEncoder.encode(request.getPassword()));
        return true;
    }
}
