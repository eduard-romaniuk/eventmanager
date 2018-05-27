package com.example.eventmanager.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final Logger logger = LogManager.getLogger(EmailService.class);

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendTextMail(String email, String subject, String text) {
        // TODO: Errors handlers
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendMailInHTML(String email, String subject, String content){
        logger.info("Start sending email for {}", email);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, false, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            message.setContent(content, "text/html");
            emailSender.send(helper.getMimeMessage());
            logger.info("Email for {} was sent", email);
        } catch (MessagingException e) {
            logger.error("Problem with sending email in HTML format - {}", e.getLocalizedMessage());
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }
    }

    public void sendMailWithAttachments(String email, String subject,
                                        String text, Map<String, DataSource> attachments) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(text);
        for (String name: attachments.keySet()) {
            helper.addAttachment(name, attachments.get(name));
        }
        emailSender.send(helper.getMimeMessage());
    }

}
