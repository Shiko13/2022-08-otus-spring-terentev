package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Homework16Application {

	public static void main(String[] args) {
		SpringApplication.run(Homework16Application.class, args);
		// http://localhost:8080
		// http://localhost:8080/actuator
		// http://localhost:8080/actuator/health
		// http://localhost:8080/actuator/logfile
		// http://localhost:8080/data-rest
		// http://localhost:8080/data-rest/explorer/index.html
	}
}