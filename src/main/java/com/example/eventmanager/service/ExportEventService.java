package com.example.eventmanager.service;

import com.example.eventmanager.dao.EventRepository;
import com.example.eventmanager.domain.Event;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExportEventService {

    private final EventService eventService;


    @Autowired
    public ExportEventService(EventService eventService) {
        this.eventService = eventService;

    }

    public JasperPrint createEventsPlan(LocalDate fromDate, LocalDate toDate, List<Event> events ) {

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

    public JasperPrint eventsPlanForExport(LocalDate fromDate, LocalDate toDate){
        List<Event> events = eventService.eventsForPeriod(fromDate, toDate);
        return createEventsPlan(fromDate, toDate,events);

    }
}
