package com.example.Shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String username){

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Welcome to Shop app");

        message.setText(
                "Hello " + username + ", \n\n" +
                "Your User has been successfully created. \n" +
                "Thank you for joining our app. \n" +
                "Shop team"
        );

        mailSender.send(message);
    }
}
