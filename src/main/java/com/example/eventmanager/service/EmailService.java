package com.example.eventmanager.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
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
    private final ExportEventService exportEventService;
    private final UserService userService;

    @Autowired
    public EmailService(JavaMailSender emailSender, ExportEventService exportEventService, UserService userService) {
        this.emailSender = emailSender;
        this.exportEventService = exportEventService;
        this.userService = userService;
    }

    public void sendTextMail(String email, String subject, String text) {
        // TODO: Errors handlers
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
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

    }


    public void sendPersonalPlanNotification(Long user_id) {

        PersonalPlanSetting personalPlanSetting = personalPlanSettingService.getPlanSetting(user_id);

            LocalDate from = personalPlanSetting.getFromDate();
            LocalDate to = from.plusDays(personalPlanSetting.getPlanPeriod());
            List<Event> events = eventService.eventsForPeriod(user_id,from,to);
            JasperPrint eventsPlan = exportEventService.createEventsPlan(from, to, events);
            sendPlan(eventsPlan,userService.getUser(user_id));
            logger.info("Personal Plan for user {} was sending",user_id);

            personalPlanSetting.setFromDate(to);
            personalPlanSettingService.updatePlanSetting(personalPlanSetting);
            logger.info("Event from date for user {} was updating, current date : {}",user_id, personalPlanSetting.getFromDate());


    }
}
