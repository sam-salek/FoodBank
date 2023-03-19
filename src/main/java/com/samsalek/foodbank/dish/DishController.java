package com.samsalek.foodbank.dish;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
@RequiredArgsConstructor
public class DishController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private final DishService dishService;

    @GetMapping(path = "{dishName}")
    public Dish getDish(@PathVariable("dishName") String name) {
        LOGGER.info("Getting the dish: " + name);
        return dishService.getDishByName(name);
    }

    @GetMapping
    public List<Dish> getDishes() {
        LOGGER.info("Getting all dishes.");
        return dishService.getDishes();
    }

    @PostMapping
    public Dish addDish(@RequestBody Dish dish) {
        LOGGER.info("Adding new dish: " + dish.getName());
        return dishService.addDish(dish);
    }

    @PutMapping(path = "{dishName}")
    public void updateDish(@PathVariable("dishName") String name,
                           @RequestParam(required = false) String newName,
                           @RequestParam(required = false) String newCategory,
                           @RequestParam(required = false) List<String> newIngredients) {
        LOGGER.info("Updating dish: " + name);
        Dish updatedDish = dishService.updateDish(name, newName, newCategory, newIngredients);
        LOGGER.info("Update successful: " + updatedDish.toString());
    }

    @DeleteMapping(path = "{dishName}")
    public void deleteDishByName(@PathVariable("dishName") String name) {
        LOGGER.info("Deleting all dishes with name: " + name);
        Long nDeleted = dishService.deleteDishByName(name);
        LOGGER.info("Deleted " + nDeleted + " dish(es) with name: " + name);
    }
}
