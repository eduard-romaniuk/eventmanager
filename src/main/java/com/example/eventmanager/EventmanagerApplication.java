package com.example.eventmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventmanagerApplication.class, args);
	}
}

/*@SpringBootApplication
public class EventmanagerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(EventmanagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		UsersRepository userDao = new UsersRepository();

		System.out.println(userDao.findOne(5l));
	}
}*/