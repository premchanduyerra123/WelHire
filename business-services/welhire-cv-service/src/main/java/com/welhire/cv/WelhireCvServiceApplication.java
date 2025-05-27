package com.welhire.cv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.welhire.persistence.repository")
@EntityScan(basePackages = "com.welhire.persistence.entity")
@ComponentScan(basePackages = {
		"com.welhire.cv",                             // your service package
		"com.welhire.persistence.repository",         // repos
		"com.welhire.shared.dto",                     // shared DTOs
		"com.welhire.persistence.entity"              // entities
})
public class WelhireCvServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WelhireCvServiceApplication.class, args);
	}

}
