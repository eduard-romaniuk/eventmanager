package com.example.eventmanager.service;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.PlanSetting;
import com.example.eventmanager.domain.User;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
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
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;


@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final ExportEventService exportEventService;
    private final UserService userService;
    private final PlanSettingService planSettingService;
    private final EventService eventService;
    private final Logger logger = LogManager.getLogger(EmailService.class);

    @Autowired
    public EmailService(JavaMailSender emailSender, ExportEventService exportEventService, UserService userService, PlanSettingService planSettingService, EventService eventService) {
        this.emailSender = emailSender;
        this.exportEventService = exportEventService;
        this.userService = userService;
        this.planSettingService = planSettingService;
        this.eventService = eventService;
    }

    public void sendVerificationLink(String email, String token) {
        // TODO: Errors handlers
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email verification");
        message.setText("Please verify your email:\nhttp://localhost:4200/email-verification/" + token);
        emailSender.send(message);
    }

    public void sendPlan(JasperPrint eventsPlan, User user) {

        MimeMessage message = emailSender.createMimeMessage();
        String email = user.getEmail();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(eventsPlan, baos);
            DataSource aAttachment = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Events plan");
            helper.addAttachment("events-plan.pdf", aAttachment);
            helper.setText("Personal events plan");
            emailSender.send(helper.getMimeMessage());
        } catch (MessagingException | JRException e) {
            e.printStackTrace();
        }
    }

    public void sendEventsPlanExport(LocalDate fromDate, LocalDate toDate) {

        JasperPrint eventsPlan = exportEventService.eventsPlanForExport(fromDate, toDate);
        sendPlan(eventsPlan,userService.getCurrentUser());
    }


    public void sendPersonalPlanNotification(Long user_id) {

        PlanSetting planSetting = planSettingService.getPlanSetting(user_id);

        if (planSetting.isSendPlan()) {

            LocalDate from = planSetting.getFromDate();
            LocalDate to = from.plusDays(planSetting.getPlanPeriod());
            List<Event> events = eventService.eventsForPeriod(user_id,from,to);
            JasperPrint eventsPlan = exportEventService.createEventsPlan(from, to, events);
            sendPlan(eventsPlan,userService.getUser(user_id));

            logger.info("Personal Plan for user {} was sending",user_id);

            planSetting.setFromDate(to);
            planSettingService.updatePlanSetting(planSetting);
            logger.info("Event from date for user {} was updating, current date : {}",user_id,planSetting.getFromDate());


        } else {
            logger.info("Sending Personal Plan disable");
        }
    }
}
