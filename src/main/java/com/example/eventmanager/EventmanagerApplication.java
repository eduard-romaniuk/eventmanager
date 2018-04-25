package com.example.eventmanager;

import com.example.eventmanager.domain.User;
import com.example.eventmanager.service.EmailService;
import com.example.eventmanager.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:users.queries.properties")
public class EventmanagerApplication /*implements CommandLineRunner*/ {

//	@Autowired
//	private EmailService emailService;
//
//    @Autowired
//    private SecurityService securityService;

	public static void main(String[] args) {
		SpringApplication.run(EventmanagerApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//	    User user = new User();
//        String username = "eduard";
//	    user.setUsername(username);
//	    String pass = "pass";
//        user.setPassword(pass);
//        user = securityService.encodePass(user);
//
//		System.out.println("--- test email sender ---");
//		try {
//            emailService.sendSimpleMessage(
//                    "eduard.romanyuk1990@gmail.com",
//                    "test mail",
//                    new StringBuilder()
//                            .append("username: ")
//                            .append(username)
//                            .append("pass: ")
//                            .append(pass)
//                            .append("hashed pass: ")
//                            .append(user.getPassword())
//                            .append("token: ")
//                            .append(securityService.encodePass(user))
//                            .toString()
//            );
//        } catch (Exception e) {
//            System.out.println("exception");
//            e.printStackTrace();
//        }
//        System.out.println("--- email sended ---");
//		System.exit(0);
//	}
}
