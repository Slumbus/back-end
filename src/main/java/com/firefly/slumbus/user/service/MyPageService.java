package com.firefly.slumbus.user.service;

import com.firefly.slumbus.user.dto.response.MyPageResponseDTO;

public interface MyPageService {
    MyPageResponseDTO getUserById();
    MyPageResponseDTO updateProfile(String imageURL);
}
