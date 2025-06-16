package com.welhire.cv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.welhire.persistence.repository.mongo")
@EnableJpaRepositories( basePackages = "com.welhire.persistence.repository.sql")
@EntityScan(basePackages = "com.welhire.persistence.entity.*")
@ComponentScan(basePackages = {
		"com.welhire.cv",                             // your service package
		"com.welhire.persistence.repository",         // repos
		"com.welhire.shared.dto",                     // shared DTOs
		"com.welhire.persistence.entity"              // entities
})
@EnableFeignClients
@EnableAsync
public class WelhireCvServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(WelhireCvServiceApplication.class, args);
	}

}
