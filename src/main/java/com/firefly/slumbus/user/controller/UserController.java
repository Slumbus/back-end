package com.firefly.slumbus.user.controller;

import com.firefly.slumbus.base.code.ErrorCode;
import com.firefly.slumbus.base.code.ResponseCode;
import com.firefly.slumbus.base.dto.ErrorResponseDTO;
import com.firefly.slumbus.base.dto.ResponseDTO;
import com.firefly.slumbus.user.dto.response.LoginResponseDTO;
import com.firefly.slumbus.user.dto.request.UserRequestDTO;
import com.firefly.slumbus.user.service.RegisterService;
import com.firefly.slumbus.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final RegisterService registerService;
    private final UserService userService;

    public UserController(RegisterService registerService, UserService userService) {
        this.registerService = registerService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDTO userDto) {
        try {
            Long userId = registerService.registerUser(userDto);

            return ResponseEntity
                    .status(ResponseCode.SUCCESS_REGISTER.getStatus().value())
                    .body(new ResponseDTO<>(ResponseCode.SUCCESS_REGISTER, userId));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(ErrorCode.USER_ALREADY_EXIST.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.USER_ALREADY_EXIST));
        }
    }

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestParam("email") String email) {
        registerService.sendCodeToEmail(email);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_SEND_CODE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_SEND_CODE, email));
    }

    @PostMapping("/resend-email")
    public ResponseEntity<?> reSendEmail(@RequestParam("email") String email) {
        registerService.resendCodeToEmail(email);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_SEND_CODE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_SEND_CODE, email));
    }

    @GetMapping("/check-code")
    public ResponseEntity verificationEmail(@RequestParam("email") String email,
                                            @RequestParam("code") String authCode) {
        boolean response = registerService.verifiedCode(email, authCode);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CHECK_CODE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CHECK_CODE, response));
    }

    @GetMapping("/check-code-password")
    public ResponseEntity verificationEmailAndFindPassword(@RequestParam("email") String email,
                                            @RequestParam("code") String authCode) {
        boolean response = registerService.verifiedCodeToFindPassword(email, authCode);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CHECK_CODE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CHECK_CODE, response));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserRequestDTO userDto) {
        try{
            LoginResponseDTO loggedInUser = userService.loginUser(userDto);

            return ResponseEntity
                    .status(ResponseCode.SUCCESS_LOGIN.getStatus().value())
                    .body(new ResponseDTO<>(ResponseCode.SUCCESS_LOGIN, loggedInUser));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(ErrorCode.USER_NOT_FOUND.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.USER_NOT_FOUND));
        }
    }

    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody UserRequestDTO userDto) {
        boolean response = userService.patchPassword(userDto);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_PASSWORD.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_PASSWORD, response));
    }
}
