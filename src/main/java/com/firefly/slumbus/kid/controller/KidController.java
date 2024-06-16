package com.firefly.slumbus.kid.controller;

import com.firefly.slumbus.base.code.ResponseCode;
import com.firefly.slumbus.base.dto.ResponseDTO;
import com.firefly.slumbus.kid.dto.KidRequestDTO;
import com.firefly.slumbus.kid.dto.KidResponseDTO;
import com.firefly.slumbus.kid.entity.KidEntity;
import com.firefly.slumbus.kid.service.KidService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.firefly.slumbus.base.UserAuthorizationUtil.getCurrentUserId;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kid")
public class KidController {

    private final KidService kidService;

    @PostMapping("")
    public ResponseDTO<KidResponseDTO> registerKid(@RequestBody KidRequestDTO kidRequestDTO) {
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

    @DeleteMapping("/{kidId}")
    public ResponseDTO<Long> deleteKid(@PathVariable("kidId") Long kidId) {
        kidService.deleteKid(kidId);
        return new ResponseDTO<>(ResponseCode.SUCCESS_DELETE_KID, kidId);
    }
}
