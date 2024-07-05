package com.firefly.slumbus.user.controller;

import com.firefly.slumbus.base.code.ResponseCode;
import com.firefly.slumbus.base.config.S3Service;
import com.firefly.slumbus.base.dto.ResponseDTO;
import com.firefly.slumbus.user.dto.response.MyPageResponseDTO;
import com.firefly.slumbus.user.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.firefly.slumbus.base.UserAuthorizationUtil.getCurrentUserId;

@RestController
@RequestMapping("/api/my-page")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;
    private final S3Service s3Service;

    @GetMapping
    public ResponseDTO<MyPageResponseDTO> getUser(){
        MyPageResponseDTO myPageResponseDTO = myPageService.getUserById();
        return new ResponseDTO<>(ResponseCode.SUCCESS_GET_MY_PAGE, myPageResponseDTO);
    }

    @PatchMapping
    public ResponseDTO<MyPageResponseDTO> updateProfile(@RequestPart("image") MultipartFile profileImage) {
        String imageURL = s3Service.uploadImage(profileImage);
        MyPageResponseDTO myPageResponseDTO = myPageService.updateProfile(imageURL);
        return new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_PROFILE, myPageResponseDTO);
    }

    @PatchMapping("/password")
    public ResponseDTO<?> changePassword(@RequestParam("origin") String originPassword,
                                         @RequestParam("new") String newPassword ) {
        Long userId = getCurrentUserId();
        boolean response = myPageService.patchPassword(userId, originPassword, newPassword);
        return new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_PASSWORD, response);

    }
}
