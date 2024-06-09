package com.firefly.slumbus.kid.controller;

import com.firefly.slumbus.kid.entity.KidEntity;
import com.firefly.slumbus.kid.service.KidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kids")
public class KidContoller {

    @Autowired
    private KidService kidService;

    @PostMapping("/register")
    public KidEntity registerKid(@RequestBody KidEntity kid) {
        return kidService.registerKid(kid);
    }
}
