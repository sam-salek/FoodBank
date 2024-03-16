package com.samsalek.foodbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class FoodBank {

	public void run(String[] args) {
		SpringApplication.run(FoodBank.class, args);
	}

	@GetMapping("/")
	public String root() {
		return "FoodBank backend is online";
	}

	@GetMapping("/api/v1")
	public String api() {
		return "FoodBank API is up and running!";
	}
}
