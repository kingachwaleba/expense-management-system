package com.team.backend.service;

import com.team.backend.model.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class JavaMailServiceImpl implements JavaMailService {

    private final JavaMailSender javaMailSender;

    public JavaMailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMessage(User user, String appUrl) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("temp80832@gmail.com");
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Reset Password");
        simpleMailMessage.setText("To reset your password, click the link below:\n"
                + appUrl + ":8080/account/reset-password?token=" + user.getToken());

        javaMailSender.send(simpleMailMessage);
    }
}
