package com.firefly.slumbus.kid.controller;

import com.firefly.slumbus.base.code.ResponseCode;
import com.firefly.slumbus.base.dto.ResponseDTO;
import com.firefly.slumbus.kid.dto.KidRequestDTO;
import com.firefly.slumbus.kid.dto.KidResponseDTO;
import com.firefly.slumbus.kid.entity.KidEntity;
import com.firefly.slumbus.kid.service.KidService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
