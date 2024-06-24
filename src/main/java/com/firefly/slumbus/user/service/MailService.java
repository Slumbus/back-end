package com.firefly.slumbus.user.service;

import com.firefly.slumbus.base.code.ErrorCode;
import com.firefly.slumbus.base.exception.ConflictException;
import com.firefly.slumbus.base.exception.InvalidValueException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.javamail.JavaMailSender;

import static com.firefly.slumbus.base.code.ErrorCode.MAIL_FAIL_ERROR;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;
    private final RedisService redisService;

    public void sendEmail(String toEmail, String title, String text) {

        // 이미 인증코드를 보낸 메일인 지 확인 (Redis에서)
        String emailInRedis = redisService.getValues(toEmail);

        if (redisService.checkExistsValue(emailInRedis)) {
            log.debug("이미 존재하는 이메일: {}", toEmail);
            throw new ConflictException(ErrorCode.DUPLICATE_MAIL);
        }

        try {
            MimeMessage message = createEmailForm(toEmail, title, text);
            emailSender.send(message); // 메일 전송
        } catch (MessagingException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
            throw new InvalidValueException(MAIL_FAIL_ERROR);
        }
    }

    // 발신할 이메일 데이터 세팅
    private MimeMessage createEmailForm(String toEmail, String title, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(text);

        return message;
    }

}
