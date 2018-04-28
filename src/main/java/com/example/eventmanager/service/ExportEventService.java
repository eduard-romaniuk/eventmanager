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
    public final JavaMailSender emailSender;

    @Autowired
    public ExportEventService(EventRepository eventRepository, JavaMailSender emailSender) {
        this.eventRepository = eventRepository;
        this.emailSender = emailSender;
    }

    public JasperPrint createEventsPlan(Long id, LocalDate fromDate, LocalDate toDate) {

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

    public void sendEventsPlan(String email,JasperPrint jasperPrint){

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
            DataSource aAttachment =  new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
            helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("1");
            helper.addAttachment("plan.pdf", aAttachment);
            helper.setText("Some text");
            emailSender.send(helper.getMimeMessage());
        } catch (MessagingException | JRException e) {
            e.printStackTrace();
        }

    }

}
