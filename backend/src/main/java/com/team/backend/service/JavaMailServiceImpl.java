package com.team.backend.service;

import com.team.backend.config.ErrorMessage;
import com.team.backend.model.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class JavaMailServiceImpl implements JavaMailService {

    private final JavaMailSender javaMailSender;
    private final ErrorMessage errorMessage;

    public JavaMailServiceImpl(JavaMailSender javaMailSender, ErrorMessage errorMessage) {
        this.javaMailSender = javaMailSender;
        this.errorMessage = errorMessage;
    }

    @Override
    public void sendMessage(User user, String appUrl) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("temp80832@gmail.com");
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject(errorMessage.get("mail.subject"));
        simpleMailMessage.setText(errorMessage.get("mail.text") + "\n" + appUrl
                + ":3000/account/reset-password?token=" + user.getToken());

        javaMailSender.send(simpleMailMessage);
    }
}
