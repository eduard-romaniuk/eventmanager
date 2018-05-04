package com.example.eventmanager.service;

import com.example.eventmanager.domain.PlanSetting;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;


@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final ExportEventService exportEventService;
    private final UserService userService;
    private final PlanSettingService planSettingService;
    private final Logger logger = LogManager.getLogger(EmailService.class);

    @Autowired
    public EmailService(JavaMailSender emailSender, ExportEventService exportEventService, UserService userService, PlanSettingService planSettingService) {
        this.emailSender = emailSender;
        this.exportEventService = exportEventService;
        this.userService = userService;
        this.planSettingService = planSettingService;
    }

    public void sendVerificationLink(String email, String token) {
        // TODO: Errors handlers
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email verification");
        message.setText("Please verify your email:\nhttp://localhost:4200/email-verification/" + token);
        emailSender.send(message);
    }

    public void sendEventsPlan(LocalDate fromDate, LocalDate toDate){

        JasperPrint eventsPlan = exportEventService.createEventsPlan(fromDate, toDate);
        MimeMessage message = emailSender.createMimeMessage();
        String email = userService.getCurrentUser().getEmail();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(eventsPlan, baos);
            DataSource aAttachment =  new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Events plan");
            helper.addAttachment("events-plan.pdf", aAttachment);
            helper.setText("Events plan from: "+fromDate+" to: "+toDate);
            emailSender.send(helper.getMimeMessage());
        } catch (MessagingException | JRException e) {
            e.printStackTrace();
        }

    }

    private final String TIME_TO_SEND_NOTIFICATIONS = "*/10 * * * * *"; //every 10 seconds

    @Scheduled(cron = TIME_TO_SEND_NOTIFICATIONS)
    public void sendEventPlanNotification() {


    }

    public void senPlan(PlanSetting planSetting){
        planSetting = planSettingService.getPlanSetting();

        if (planSetting.isSendPlan()){

            LocalDate from = planSetting.getFrom();


        } else {

        }
    }
}
