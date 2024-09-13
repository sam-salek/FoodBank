package io.github.samsalmag.foodbankbackend.dishservice.controller;

import io.github.samsalmag.foodbankbackend.dishservice.dto.DishRequestDTO;
import io.github.samsalmag.foodbankbackend.dishservice.dto.DishResponseDTO;
import io.github.samsalmag.foodbankbackend.dishservice.exception.DishNotFoundException;
import io.github.samsalmag.foodbankbackend.dishservice.exception.InvalidDishNameException;
import io.github.samsalmag.foodbankbackend.dishservice.service.DishService;
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

    @GetMapping("/")
    public String root() {
        return "FoodBank's dish service is up and running!";
    }

    @GetMapping("/all")
    public List<DishResponseDTO> getAllDishes() {
        List<DishResponseDTO> dishes = dishService.getAllDishes();
        LOGGER.info("Getting all dishes.");
        return dishes;
    }

    @GetMapping("/name/{dishName}")
    public DishResponseDTO getDishByName(@PathVariable("dishName") String dishName) {
        DishResponseDTO dish = dishService.getDishByName(dishName);
        LOGGER.info("Getting dish with name: " + dishName);
        return dish;
    }

    @GetMapping("/id/{id}")
    public DishResponseDTO getDishById(@PathVariable("id") String id) {
        DishResponseDTO dish = dishService.getDishById(id);
        LOGGER.info("Getting dish with id: " + id);
        return dish;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public DishResponseDTO addDish(@RequestBody DishRequestDTO dishRequest) {
        DishResponseDTO newDish = dishService.addDish(dishRequest);
        LOGGER.info("Added new dish: " + newDish.getName());
        return newDish;
    }

    @PutMapping("/{id}")
    public DishResponseDTO updateDish(@PathVariable("id") String id, @RequestBody DishRequestDTO dishRequest) {
        DishResponseDTO updatedDish = dishService.updateDish(id, dishRequest);
        LOGGER.info("Updated dish with id: " + id + ", with data of: " + dishRequest.toString());
        return updatedDish;
    }

    @DeleteMapping("/{id}")
    public String deleteDishById(@PathVariable("id") String id) {
        dishService.deleteDishById(id);
        LOGGER.info("Deleted dish with id: " + id);
        return "Dish deleted successfully!";
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
