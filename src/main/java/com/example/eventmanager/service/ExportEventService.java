package com.example.eventmanager.service;

import com.example.eventmanager.domain.Event;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExportEventService {

    private final Logger logger = LogManager.getLogger(ExportEventService.class);

    private final EventService eventService;
    private final UserService userService;
    private final EmailService emailService;


    @Autowired
    public ExportEventService(EventService eventService, UserService userService, EmailService emailService) {
        this.eventService = eventService;
        this.userService = userService;
        this.emailService = emailService;
    }

    public JasperPrint createEventsPlan(LocalDate fromDate, LocalDate toDate, List<Event> events) {

        Map<String, Object> params = new HashMap<>();
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);

        try {
            JasperReport eventsPlan = JasperCompileManager.compileReport("src/main/resources/eventsPlan.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(events);
            return JasperFillManager.fillReport(eventsPlan, params, dataSource);

        } catch (JRException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public JasperPrint eventsPlanForExport(LocalDate fromDate, LocalDate toDate) {
        List<Event> events = eventService.eventsForPeriod(userService.getCurrentUser().getId(), fromDate, toDate);
        return createEventsPlan(fromDate, toDate, events);

    }

    public void sendEventsPlan(String email,JasperPrint eventsPlan, LocalDate fromDate, LocalDate toDate) {
        try {
            logger.info("Sending events plan via email...");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(eventsPlan, baos);
            DataSource plan = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
            emailService.sendMailWithAttachments(
                     email,
                    "Events Plan",
                    "Events plan from: " + fromDate + " to: " + toDate,
                    Collections.singletonMap("events-plan.pdf", plan));
            logger.info("Events plan was sending to {}",email);
        } catch (MessagingException | JRException e) {
            logger.info("Problem whits sending email",e.getLocalizedMessage());
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }
    }

}
