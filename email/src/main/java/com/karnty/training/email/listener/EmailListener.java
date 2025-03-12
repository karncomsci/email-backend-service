package com.karnty.training.email.listener;

import com.karnty.training.common.model.request.EmailRequest;
import com.karnty.training.email.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EmailListener {

    private final EmailService emailService;

    public EmailListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "activation-email")
    private void listenForActivationEmail(EmailRequest request) throws MessagingException {
        log.info("Kafka received: {}", request.getTo());
        this.emailService.sendMail(request);
    }
}
