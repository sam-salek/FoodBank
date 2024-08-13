package io.github.samsalmag.foodbankbackend.dish;

import io.github.samsalmag.foodbankbackend.InvalidDishNameException;
import io.github.samsalmag.foodbankbackend.DishNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dish")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class DishController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private final DishService dishService;

    @GetMapping("/all")
    public List<Dish> getDishes() {
        List<Dish> dishes = dishService.getDishes();
        LOGGER.info("Getting all dishes.");
        return dishes;
    }

    @GetMapping("/name/{dishName}")
    public Dish getDishByName(@PathVariable("dishName") String dishName) {
        Dish dish = dishService.getDishByName(dishName);
        LOGGER.info("Getting dish with name " + dishName);
        return dish;
    }

    @GetMapping("/id/{id}")
    public Dish getDishById(@PathVariable("id") String id) {
        Dish dish = dishService.getDishById(id);
        LOGGER.info("Getting dish with id " + id);
        return dish;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Dish addDish(@RequestBody Dish dish) {
        LOGGER.info("Attempting to add dish: " + dish);
        Dish newDish = dishService.addDish(dish);
        LOGGER.info("Added new dish " + newDish.getName());
        return newDish;
    }

    @PutMapping("/update/{id}")
    public Dish updateDish(@PathVariable("id") String id, @RequestBody Dish dish) {
        Dish updatedDish = dishService.updateDish(id, dish);
        LOGGER.info("Updated dish with id " + id);
        return updatedDish;
    }

    @DeleteMapping("/remove/{id}")
    public String deleteDishById(@PathVariable("id") String id) {
        dishService.deleteDishById(id);
        LOGGER.info("Deleted dish with id " + id);
        return "Dish deleted successfully";
    }

    @ExceptionHandler(DishNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleDishNotFound(HttpServletRequest req, DishNotFoundException e) {
        LOGGER.error(e.getMessage() + ". Request: " + req.getServletPath(), e);
        return "Dish not found";
    }

    @ExceptionHandler(InvalidDishNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidDishName(HttpServletRequest req, InvalidDishNameException e) {
        LOGGER.error(e.getMessage() + ". Request: " + req.getServletPath(), e);
        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(HttpServletRequest req, Exception e) {
        LOGGER.error("An error occurred while processing the request. Request: " + req.getServletPath(), e);
        return "Internal server error";
    }
}
