package com.test.OneStop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.jdbc.core.JdbcTemplate;

@EnableEurekaClient
@SpringBootApplication
public class OneStopApplication {

	@Autowired
	JdbcTemplate jd;
	
	/*
	
	Used for Inserting TestUser to check Login
	
	@PostConstruct
	public void insertSampleData()
	{
		String encodedPassword = new BCryptPasswordEncoder().encode("Pass");
		jd.execute("insert into login(email,password,role,firstname,lastname,mobile) values('mail','"+encodedPassword+"','User','name1','name2','123456789')");
	}

    */
	public static void main(String[] args) {
		SpringApplication.run(OneStopApplication.class, args);
	}

}
