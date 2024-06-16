package com.firefly.slumbus.kid.service;

import com.firefly.slumbus.kid.dto.KidRequestDTO;
import com.firefly.slumbus.kid.dto.KidResponseDTO;
import com.firefly.slumbus.kid.entity.KidEntity;

import java.util.List;


public interface KidService {
    KidResponseDTO registerKid(KidRequestDTO kidRequestDTO);
    List<KidResponseDTO> getKidList();
    KidResponseDTO getKidDetails(Long kidId);
}
