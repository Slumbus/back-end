package com.firefly.slumbus.user.service;

import com.firefly.slumbus.base.exception.BadRequestException;
import com.firefly.slumbus.base.exception.ConflictException;
import com.firefly.slumbus.user.dto.request.UserRequestDTO;
import com.firefly.slumbus.user.entity.UserEntity;
import com.firefly.slumbus.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static com.firefly.slumbus.base.code.ErrorCode.DUPLICATE_USER;
import static com.firefly.slumbus.base.code.ErrorCode.INVALID_MAIL_CODE;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final MailService mailService;

    private final RedisService redisService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public RegisterService(UserRepository userRepository, MailService mailService, RedisService redisService) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.redisService = redisService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * 회원 가입
     */
    public Long registerUser(UserRequestDTO request) {

        validateDuplicateMember(request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .picture(null) // 회원 가입 시에는 프로필 x
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return user.getUserId();
    }

    /**
     * 이미 가입된 회원 여부 확인
     */

    private void validateDuplicateMember(String email) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            throw new ConflictException(DUPLICATE_USER);
        }
    }

    /**
     * 이메일 인증
     */
    public void sendCodeToEmail(String toEmail) {
        this.validateDuplicateMember(toEmail);
        String title = "Slumbus 이메일 인증 번호";
        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = Email / value = AuthCode )
        redisService.setValues( toEmail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("코드 생성 오류");
        }
    }

    public boolean verifiedCode(String email, String authCode) {
        this.validateDuplicateMember(email);
        String redisAuthCode = redisService.getValues(email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);

        if (!authResult) {
            throw new BadRequestException(INVALID_MAIL_CODE);
        } else {
            return true;
        }
    }

}
