package com.portifolio.apibff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiBffApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiBffApplication.class, args);
	}

}
