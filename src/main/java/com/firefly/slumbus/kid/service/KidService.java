package com.firefly.slumbus.kid.service;

import com.firefly.slumbus.kid.dto.KidRequestDTO;
import com.firefly.slumbus.kid.dto.KidResponseDTO;
import com.firefly.slumbus.kid.entity.KidEntity;


public interface KidService {
    KidResponseDTO registerKid(KidRequestDTO kidRequestDTO);
}
