package ru.softplat.security.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.email.EmailMessage;
import ru.softplat.security.server.dto.UserCreateDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String email;
    private final JavaMailSender emailSender;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void sendRegConfirmationEmail(UserCreateDto userCreateDto) {
        String text = getRegConfirmEmailText(userCreateDto.getName());

        SimpleMailMessage message = createEmailMessage(userCreateDto.getEmail(), text);
        String emailSubject = String.format(EmailMessage.REG_CONFIRM_SUBJECT.body, userCreateDto.getName());
        message.setSubject(emailSubject);

        emailSender.send(message);
    }

    public void sendRestorePasswordEmail(String email) {
        String text = String.format(
                EmailMessage.RESTORE_PASSWORD_EMAIL.body,
                "https://softplat.ru/change/pass");

        SimpleMailMessage message = createEmailMessage(email, text);
        String emailSubject = String.format(EmailMessage.RESTORE_PASSWORD_SUBJECT.body);
        message.setSubject(emailSubject);

        emailSender.send(message);
    }

    private SimpleMailMessage createEmailMessage(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(to);
        message.setText(text);
        message.setSentDate(Date.from(Instant.now()));
        return message;
    }

    private String getRegConfirmEmailText(String name) {
        return String.format(
                EmailMessage.REG_CONFIRM_EMAIL.body,
                name,
                LocalDateTime.now().format(formatter));
    }
}