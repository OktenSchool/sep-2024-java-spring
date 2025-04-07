package ua.com.owu.sep2024.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import ua.com.owu.sep2024.orderservice.dto.MailDto;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(MailDto mailDto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(from);
        mailMessage.setTo(mailDto.to());

        mailMessage.setSubject(mailDto.title());
        mailMessage.setText(mailDto.message());

        mailSender.send(mailMessage);
    }
}
