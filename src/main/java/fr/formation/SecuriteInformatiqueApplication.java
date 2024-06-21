package fr.formation;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = "fr.formation")
public class SecuriteInformatiqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecuriteInformatiqueApplication.class, args);
	}
	@Bean
    public RestTemplate customRestTemplate() {
        return new RestTemplate();
    }


}
