package com.karnty.training.email.service;

import com.karnty.training.common.model.request.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailService {

    @Value("${app.email.from}")
    private String from;


    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendMail(EmailRequest request) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(request.getTo());
        mimeMessageHelper.setSubject(request.getSubject());

        Context context = new Context();
        context.setVariable("name", request.getContent().get("name"));
        context.setVariable("verificationUrl", request.getContent().get("verificationUrl"));
        String processedString = templateEngine.process("email-template.html", context);

        mimeMessageHelper.setText(processedString, true);


        mailSender.send(mimeMessage);
    }

}
