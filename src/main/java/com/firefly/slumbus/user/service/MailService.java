package com.firefly.slumbus.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;

    public void sendEmail(String toEmail, String title, String text) {
        try {
            MimeMessage message = createEmailForm(toEmail, title, text);
            emailSender.send(message);
            log.debug("전송됨");
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
            throw new IllegalArgumentException("메일 전송 오류" + e.getMessage(), e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
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
