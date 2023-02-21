package com.samsalek.foodbank;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@SpringBootApplication
@RestController
public class FoodBank {

	public void run(String[] args) {
		SpringApplication.run(FoodBank.class, args);
	}

	@GetMapping("/")
	public String getHomePage() {
		return "Hello!";
	}

	@Bean
	CommandLineRunner runner(DishRepository dishRepository) {
		return args -> {
			Dish dish = new Dish("Taco", "Mexican", List.of("Tortilla, Meat, Vegetables, Salsa"), LocalDateTime.now());
			dishRepository.insert(dish);
		};
	}
}
