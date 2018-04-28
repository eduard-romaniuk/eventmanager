package com.example.eventmanager.service;

import com.example.eventmanager.dao.EventRepository;
import com.example.eventmanager.domain.Event;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
            JasperPrint jPrint = JasperFillManager.fillReport(eventsPlan,params,dataSource);

            return jPrint;

        } catch (JRException ex){
            ex.printStackTrace();
            return null;
        }

    }


}
