package com.example.eventmanager.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class JdbcTemplateSingleton extends JdbcTemplate {
    private static JdbcTemplate ourInstance = new JdbcTemplate(dataSource());

    public static DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/eventmanagerdb");
        dataSource.setUsername("postgres");
        dataSource.setPassword("root");
        return dataSource;
    }

    public static JdbcTemplate getInstance() {
        return ourInstance;
    }


    private JdbcTemplateSingleton() {
    }
}
/* dataSource.setUrl("jdbc:postgresql://ec2-54-217-217-142.eu-west-1.compute.amazonaws.com:5432" +
         "/d4btkoj41tnlfe?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
         dataSource.setUsername("xqfzmsoqaknvri");
         dataSource.setPassword("fa270a93981995a8eabbe236fcab3a3c0b48d69e7fe3f34f474ba3065731e38a");*/