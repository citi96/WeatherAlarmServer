package com.citi.WeatherAlarmDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.citi.WeatherAlarmDB.Models")
public class WeatherAlarmDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherAlarmDbApplication.class, args);
	}

}
