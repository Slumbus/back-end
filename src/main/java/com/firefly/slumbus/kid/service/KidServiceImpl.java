package com.firefly.slumbus.kid.service;

import com.firefly.slumbus.kid.dto.KidRequestDTO;
import com.firefly.slumbus.kid.dto.KidResponseDTO;
import com.firefly.slumbus.kid.entity.Gender;
import com.firefly.slumbus.kid.entity.KidEntity;
import com.firefly.slumbus.kid.repository.KidRepository;
import com.firefly.slumbus.user.entity.UserEntity;
import com.firefly.slumbus.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.firefly.slumbus.base.UserAuthorizationUtil.getCurrentUserId;

@Service
@RequiredArgsConstructor
public class KidServiceImpl implements KidService{

    private final KidRepository kidRepository;
    private final UserRepository userRepository;

    @Override
    public KidResponseDTO registerKid(KidRequestDTO kidRequestDTO) {
        Long userId = getCurrentUserId();
        System.out.println(userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Gender gender = null;
        switch (kidRequestDTO.getGender()){
            case 0:
                gender = Gender.MALE;
                break;
            case 1:
                gender = Gender.FEMALE;
                break;
            case 2:
                gender = Gender.NONE;
                break;
        }

        KidEntity kidEntity = KidEntity.builder()
                .user(user)
                .name(kidRequestDTO.getName())
                .birth(kidRequestDTO.getBirth())
                .picture(kidRequestDTO.getPicture())
                .gender(gender)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        KidEntity savedKid = kidRepository.save(kidEntity);

        return KidResponseDTO.builder()
                .kidId(savedKid.getKidId())
                .userId(savedKid.getUser().getUserId())
                .name(savedKid.getName())
                .birth(savedKid.getBirth())
                .picture(savedKid.getPicture())
                .gender(savedKid.getGender())
                .build();
    }

    @Override
    public List<KidResponseDTO> getKidList() {
        Long userId = getCurrentUserId();

        return kidRepository.findByUser_userId(userId).stream()
                .map(kid -> KidResponseDTO.builder()
                        .userId(kid.getUser().getUserId())
                        .kidId(kid.getKidId())
                        .name(kid.getName())
                        .birth(kid.getBirth())
                        .picture(kid.getPicture())
                        .gender(kid.getGender())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public KidResponseDTO getKidDetails(Long kidId) {
        KidEntity kid = kidRepository.findById(kidId)
                .orElseThrow(() -> new RuntimeException("Kid not found or access denied"));

        return KidResponseDTO.builder()
                .userId(kid.getUser().getUserId())
                .kidId(kid.getKidId())
                .name(kid.getName())
                .birth(kid.getBirth())
                .picture(kid.getPicture())
                .gender(kid.getGender())
                .build();
    }

}
