package com.test.eurekaDash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaDashApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaDashApplication.class, args);
	}

}
