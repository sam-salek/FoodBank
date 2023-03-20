package com.samsalek.foodbank;

import com.samsalek.foodbank.dish.Dish;
import com.samsalek.foodbank.dish.DishRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInit implements ApplicationListener<ApplicationReadyEvent> {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private final DishRepository dishRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initDatabase();
    }

    private void initDatabase() {
        String dishInit = "init";
        if (!dishRepository.existsByName(dishInit)) dishRepository.insert(new Dish(dishInit));
        dishRepository.deleteByName(dishInit);
        LOGGER.info("Initialized MongoDB collection");

        // Insert test dish every time we start the app
        Dish dish = new Dish("Taco", "Mexican", List.of("Tortilla", "Beef", "Vegetables", "Salsa"), LocalDateTime.now());
        dishRepository.insert(dish);
    }
}
