package com.springone.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.springone.entity")
@EnableJpaRepositories(basePackages = "com.springone.repository")
@ComponentScan(basePackages = "com.springone.*")

@SpringBootApplication
public class SpringoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringoneApplication.class, args);
	}

}
