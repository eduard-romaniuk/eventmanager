package com.example.eventmanager.service;

import com.example.eventmanager.domain.Event;
import com.example.eventmanager.domain.User;
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
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final ExportEventService exportEventService;
    private final UserService userService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

    @Autowired
    public EmailService(JavaMailSender emailSender, ExportEventService exportEventService, UserService userService) {
        this.emailSender = emailSender;
        this.exportEventService = exportEventService;
        this.userService = userService;
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

    public void sendEventNotification(User user, List<Event> eventsWithoutCountdown, List<Event> eventsWithCountdown) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Notification about your events");

        String messageText = "Hello, " + user.getLogin() + "! \n\n";

        if(eventsWithoutCountdown.size() > 0){
            messageText += "Your upcoming events:\n";
            for (Event event : eventsWithoutCountdown){
                String eventInfo = "Event \'" + event.getName() + "\'\n" +
                        "Start at " + event.getTimeLineStart().format(formatter) + "\n" +
                        "End at " + event.getTimeLineFinish().format(formatter) + "\n\n";
                messageText += eventInfo;
            }
        }

        if(eventsWithCountdown.size() > 0){
            messageText += "Countdown to your selected events:\n";
            for (Event event : eventsWithCountdown){
                long countDown = DAYS.between(LocalDate.now(), event.getTimeLineStart().toLocalDate());

                String eventInfo = "Event \'" + event.getName() + "\'\n" +
                        "Start at " + event.getTimeLineStart().format(formatter) + "\n" +
                        "End at " + event.getTimeLineFinish().format(formatter) + "\n" +
                        "Left " + countDown + " days\n\n";
                messageText += eventInfo;
            }
        }

        messageText += "Have fun! \nYour Event manager team";
        message.setText(messageText);
        emailSender.send(message);
    }
}
