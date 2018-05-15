package com.example.eventmanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@SpringBootApplication
@EnableScheduling
public class EventmanagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventmanagerApplication.class, args);
    }

}
