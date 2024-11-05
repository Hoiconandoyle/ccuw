package com.mailserver;
import com.mailserver.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MailServerApplication {
	public static void main(String[] args) {
		 SpringApplication.run(MailServerApplication.class, args);
		 UserService.getInstance().loadUsers();
	}
}
