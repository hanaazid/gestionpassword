package fr.formation;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "fr.formation")
public class SecuriteInformatiqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecuriteInformatiqueApplication.class, args);
	}

}
