package com.samsalek.foodbankbackend;

import com.samsalek.foodbankbackend.dish.Dish;
import com.samsalek.foodbankbackend.dish.DishRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseService implements ApplicationListener<ApplicationReadyEvent> {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private final DishRepository dishRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initDatabase();
    }

    private void initDatabase() {
        // At least one document must be put into a collection for the collection to be created (if the collection doesn't exist already)
        Dish init = new Dish();
        dishRepository.insert(init);
        dishRepository.delete(init);
        LOGGER.info("Initialized MongoDB collection.");

        // Inserts test dish every time we start the app
        // Dish dish = new Dish("Taco", "Mexican", List.of("Tortilla", "Beef", "Vegetables", "Salsa"), List.of("Put it all in the bread.", "Eat."));
        // dishRepository.insert(dish);
    }
}
