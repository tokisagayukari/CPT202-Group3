package com.example.demo.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private TemplateEngine templateEngine;

    public void sendMailForActivation(String activationUrl, String email){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true);
            message.setSubject("Notice for Activating Account ");
            message.setFrom(mailUsername);
            message.setTo(email);
            message.setSentDate(new Date());
            Context context = new Context();
            context.setVariable("activationUrl",activationUrl);
            String text = templateEngine.process("activation_account.html",context);
            message.setText(text,true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }

    public void sendMailForResetPassword(String activationUrl, String email){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true);
            message.setSubject("Notice for Resetting your password ");
            message.setFrom(mailUsername);
            message.setTo(email);
            message.setSentDate(new Date());
            Context context = new Context();
            context.setVariable("activationUrl",activationUrl);
            String text = templateEngine.process("activation_account.html",context);
            message.setText(text,true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }
}
