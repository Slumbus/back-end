package com.firefly.slumbus.user.controller;

import com.firefly.slumbus.base.code.ResponseCode;
import com.firefly.slumbus.base.dto.ResponseDTO;
import com.firefly.slumbus.user.dto.response.MyPageResponseDTO;
import com.firefly.slumbus.user.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.firefly.slumbus.base.UserAuthorizationUtil.getCurrentUserId;

@RestController
@RequestMapping("/api/my-page")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping
    public ResponseDTO<MyPageResponseDTO> getUser(){
        Long userId = getCurrentUserId();
        MyPageResponseDTO myPageResponseDTO = myPageService.getUserById(userId);
        return new ResponseDTO<>(ResponseCode.SUCCESS_GET_MY_PAGE, myPageResponseDTO);
    }
}
