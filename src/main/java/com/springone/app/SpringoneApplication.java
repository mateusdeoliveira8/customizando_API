package com.springone.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@EntityScan(basePackages = "com.springone.entity")
@EnableJpaRepositories(basePackages = "com.springone.repository")
@ComponentScan(basePackages = "com.springone.*")

@Configuration
@SpringBootApplication
public class SpringoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringoneApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
