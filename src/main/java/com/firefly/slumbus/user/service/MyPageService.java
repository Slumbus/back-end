package com.firefly.slumbus.user.service;

import com.firefly.slumbus.user.dto.response.MyPageResponseDTO;

public interface MyPageService {
    MyPageResponseDTO getUserById(Long userId);

    boolean patchPassword(Long userId, String originPassword,String newPassword);
}
