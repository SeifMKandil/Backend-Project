package com.sumerge.moviesMicroservice;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class MoviesMicroserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MoviesMicroserviceApplication.class, args);
	}


}
