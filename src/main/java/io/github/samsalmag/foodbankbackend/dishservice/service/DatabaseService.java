package io.github.samsalmag.foodbankbackend.dishservice.service;

import io.github.samsalmag.foodbankbackend.dishservice.model.Dish;
import io.github.samsalmag.foodbankbackend.dishservice.repository.DishRepository;
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
        initDishesCollection();
    }

    private void initDishesCollection() {
        // At least one document must be put into a collection for the collection to be created (if the collection doesn't exist already)
        Dish init = new Dish();
        dishRepository.insert(init);
        dishRepository.delete(init);
        LOGGER.info("Initialized \"dishes\" MongoDB collection.");
    }
}
