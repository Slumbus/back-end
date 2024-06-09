package com.firefly.slumbus.kid.service;

import com.firefly.slumbus.kid.entity.KidEntity;
import com.firefly.slumbus.kid.repository.KidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class KidService {
    @Autowired
    private KidRepository kidRepository;

    public KidEntity registerKid(KidEntity kid) {
        kid.setCreatedAt(LocalDateTime.now());
        kid.setUpdatedAt(LocalDateTime.now());
        return kidRepository.save(kid);
    }
}
