package com.samsalek.foodbank.dish;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dishes")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class DishController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private final DishService dishService;

    @GetMapping()
    public ResponseEntity<List<Dish>> getDishes() {
        List<Dish> dishes = dishService.getDishes();
        LOGGER.info("Getting all dishes.");
        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }

    @GetMapping("/name/{dishName}")
    public ResponseEntity<Dish> getDishByName(@PathVariable("dishName") String dishName) {
        Dish dish = dishService.getDishByName(dishName);
        LOGGER.info("Getting dish with name: " + dishName);
        return new ResponseEntity<>(dish, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable("id") String id) {
        Dish dish = dishService.getDishById(id);
        LOGGER.info("Getting dish with id: " + id);
        return new ResponseEntity<>(dish, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDish(@RequestBody Dish dish) {
        dishService.addDish(dish);
        LOGGER.info("Added new dish: " + dish.getName());
        return new ResponseEntity<>("Dish added successfully", HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<String> updateDish(@PathVariable("id") String id, @RequestBody Dish dish) {
        dishService.updateDish(id, dish);
        LOGGER.info("Updated dish with id: " + id);
        return new ResponseEntity<>("Dish updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDishById(@PathVariable("id") String id) {
        dishService.deleteDishById(id);
        LOGGER.info("Deleted dish with id: " + id);
        return new ResponseEntity<>("Dish deleted successfully", HttpStatus.OK);
    }
}
