package com.example.eventmanager.service;

import com.example.eventmanager.dao.EventRepository;
import com.example.eventmanager.domain.Event;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExportEventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final JavaMailSender emailSender;

    @Autowired
    public ExportEventService(EventRepository eventRepository,UserService userService, JavaMailSender emailSender) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.emailSender = emailSender;
    }

    public JasperPrint createEventsPlan(LocalDate fromDate, LocalDate toDate) {

        Long id = userService.findUser(userService.getCurrentUsername()).getId();
        List<Event> events = eventRepository.eventsListForPeriod(id, fromDate, toDate);

        Map<String,Object> params=new HashMap<>();
        params.put("fromDate",fromDate);
        params.put("toDate",toDate);

        try {
            JasperReport eventsPlan = JasperCompileManager.compileReport("src/main/resources/eventsPlan.jrxml");
            JRBeanCollectionDataSource dataSource= new JRBeanCollectionDataSource(events);
            return JasperFillManager.fillReport(eventsPlan,params,dataSource);

        } catch (JRException ex){
            ex.printStackTrace();
            return null;
        }

    }

    public void sendEventsPlan(LocalDate fromDate, LocalDate toDate){

        JasperPrint eventsPlan = createEventsPlan(fromDate, toDate);
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        String email = userService.findUser(userService.getCurrentUsername()).getEmail();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(eventsPlan, baos);
            DataSource aAttachment =  new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
            helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Events plan");
            helper.addAttachment("events-plan.pdf", aAttachment);
            helper.setText("Events plan from: "+fromDate+"to: "+toDate);
            emailSender.send(helper.getMimeMessage());
        } catch (MessagingException | JRException e) {
            e.printStackTrace();
        }

    }

}
