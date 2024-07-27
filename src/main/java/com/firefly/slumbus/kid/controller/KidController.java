package com.firefly.slumbus.kid.controller;

import com.firefly.slumbus.base.code.ResponseCode;
import com.firefly.slumbus.base.config.S3Service;
import com.firefly.slumbus.base.dto.ResponseDTO;
import com.firefly.slumbus.kid.dto.KidRequestDTO;
import com.firefly.slumbus.kid.dto.KidResponseDTO;
import com.firefly.slumbus.kid.entity.KidEntity;
import com.firefly.slumbus.kid.service.KidService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.firefly.slumbus.base.UserAuthorizationUtil.getCurrentUserId;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kid")
public class KidController {

    private final KidService kidService;
    private final S3Service s3Service;

    @PostMapping("")
    public ResponseDTO<KidResponseDTO> registerKid(@RequestPart("kidDTO") KidRequestDTO kidRequestDTO,
                                                   @RequestPart("image") MultipartFile kidImage) {

        String imageURL = s3Service.uploadImage(kidImage);
        kidRequestDTO.setPicture(imageURL);
        KidResponseDTO registeredKid = kidService.registerKid(kidRequestDTO);
        return new ResponseDTO<>(ResponseCode.SUCCESS_REGISTER_KID, registeredKid);
    }

    @GetMapping("")
    public ResponseDTO<List<KidResponseDTO>> getKidList() {
        List<KidResponseDTO> kidList = kidService.getKidList();
        return new ResponseDTO<>(ResponseCode.SUCCESS_GET_KID_LIST, kidList);
    }

    @GetMapping("/{kidId}")
    public ResponseDTO<KidResponseDTO> getKidDetails(@PathVariable("kidId") Long kidId) {
        KidResponseDTO kidDetails = kidService.getKidDetails(kidId);
        return new ResponseDTO<>(ResponseCode.SUCCESS_GET_KID_DETAIL, kidDetails);
    }

    @PatchMapping("/{kidId}")
    public ResponseDTO<KidResponseDTO> updateKid(
            @PathVariable("kidId") Long kidId,
            @RequestPart(value = "image", required = false) MultipartFile kidImage,
            @RequestPart("kidDTO") KidRequestDTO kidRequestDTO) {
        try {
            String imageURL = null;
            if (kidImage != null && !kidImage.isEmpty()) {
                imageURL = s3Service.uploadImage(kidImage);
                kidRequestDTO.setPicture(imageURL);
            }
            KidResponseDTO updateKid = kidService.updateKid(kidId, kidRequestDTO);
            return new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_KID, updateKid);
        } catch (Exception e) {
            String message = e.getMessage();
            return new ResponseDTO<>(0, "실패", message, null);
        }
    }

    @DeleteMapping("/{kidId}")
    public ResponseDTO<Long> deleteKid(@PathVariable("kidId") Long kidId) {
        kidService.deleteKid(kidId);
        return new ResponseDTO<>(ResponseCode.SUCCESS_DELETE_KID, kidId);
    }
}
