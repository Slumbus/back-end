package com.firefly.slumbus.user.service;

import com.firefly.slumbus.base.exception.InvalidValueException;
import com.firefly.slumbus.user.dto.response.MyPageResponseDTO;
import com.firefly.slumbus.user.entity.UserEntity;
import com.firefly.slumbus.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.firefly.slumbus.base.UserAuthorizationUtil.getCurrentUserId;
import static com.firefly.slumbus.base.code.ErrorCode.INVALID_ORIGIN_PASSWORD;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MyPageResponseDTO getUserById() {
        Long userId = getCurrentUserId();
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다." + userId));
        return new MyPageResponseDTO(userEntity.getEmail(), userEntity.getPicture());
    }

    @Override
    public MyPageResponseDTO updateProfile(String imageURL) {
        Long userId = getCurrentUserId();
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다." + userId));
        userEntity.setPicture(imageURL);

        userRepository.save((userEntity));

        return new MyPageResponseDTO(userEntity.getEmail(), userEntity.getPicture());
    }

    @Override
    public boolean patchPassword(Long userId, String originPassword, String newPassword) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다." + userId));

        if (!passwordEncoder.matches(originPassword, userEntity.getPassword())) {
            throw new InvalidValueException(INVALID_ORIGIN_PASSWORD);
        } else {
            userEntity.updatePassword(passwordEncoder.encode(newPassword));

            userRepository.save(userEntity);
        }

        return true;
    }
}
