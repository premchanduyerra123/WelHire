package com.welhire.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class WelhireDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(WelhireDiscoveryApplication.class, args);
	}

}
