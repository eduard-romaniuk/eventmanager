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

    @Autowired
    public ExportEventService(EventRepository eventRepository,UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    public JasperPrint createEventsPlan(LocalDate fromDate, LocalDate toDate) {

        Long id = userService.getCurrentUser().getId();
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
}
