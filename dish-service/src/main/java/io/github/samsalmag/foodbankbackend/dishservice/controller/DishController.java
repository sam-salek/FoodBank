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

    /**
     * Root of the service. Can be used to check if the service is running.
     *
     * @return A message indicating the status of the service.
     */
    @GetMapping("/")
    public String root() {
        return "FoodBank's dish service is up and running!";
    }

    /**
     * Fetches all dishes.
     *
     * @return A List of all dishes.
     */
    @GetMapping("/all")
    public List<DishResponseDTO> getAllDishes() {
        List<DishResponseDTO> dishes = dishService.getAllDishes();
        LOGGER.info("Getting all dishes.");
        return dishes;
    }

    /**
     * Fetches a dish by name.
     *
     * @param dishName Name of the dish to be fetched.
     * @return Dish with the provided name.
     * @throws DishNotFoundException if dish with name of {@code dishName} does not exist.
     */
    @GetMapping("/name/{dishName}")
    public DishResponseDTO getDishByName(@PathVariable("dishName") String dishName) {
        DishResponseDTO dish = dishService.getDishByName(dishName);
        LOGGER.info("Getting dish with name: " + dishName);
        return dish;
    }

    /**
     * Fetches a dish by ID.
     *
     * @param id ID of the dish to be fetched.
     * @return Dish with the provided ID.
     * @throws DishNotFoundException if dish with ID of {@code id} does not exist.
     */
    @GetMapping("/id/{id}")
    public DishResponseDTO getDishById(@PathVariable("id") String id) {
        DishResponseDTO dish = dishService.getDishById(id);
        LOGGER.info("Getting dish with id: " + id);
        return dish;
    }

    /**
     * Adds a new dish.
     *
     * @param dishRequest Requested dish to be created.
     * @return The newly created dish object.
     * @throws InvalidDishNameException if name of {@code dishRequest} is either {@code null}, empty, or already taken by another dish.
     */
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public DishResponseDTO addDish(@RequestBody DishRequestDTO dishRequest) {
        DishResponseDTO newDish = dishService.addDish(dishRequest);
        LOGGER.info("Added new dish: " + newDish.getName());
        return newDish;
    }

    /**
     * Updates an existing dish with new data.
     *
     * @param id ID of the dish to update.
     * @param dishRequest Requested dish to replace the existing dish with ID of {@code id}
     * @return The updated dish with its new data.
     * @throws DishNotFoundException if dish to update (dish with ID of {@code id}) does not exist.
     * @throws InvalidDishNameException if name of {@code dishRequest} is either {@code null}, empty, [TODO: or already taken by another dish]
     */
    @PutMapping("/{id}")
    public DishResponseDTO updateDish(@PathVariable("id") String id, @RequestBody DishRequestDTO dishRequest) {
        DishResponseDTO updatedDish = dishService.updateDish(id, dishRequest);
        LOGGER.info("Updated dish with id: " + id + ", with data of: " + dishRequest.toString());
        return updatedDish;
    }

    /**
     * Deletes dish by ID.
     * @param id ID of the dish to delete.
     * @return A message indicating that the dish was successfully deleted.
     * @throws DishNotFoundException if dish with ID of {@code id} does not exist.
     */
    @DeleteMapping("/{id}")
    public String deleteDishById(@PathVariable("id") String id) {
        dishService.deleteDishById(id);
        LOGGER.info("Deleted dish with id: " + id);
        return "Dish deleted successfully!";
    }

    /**
     * Handles {@link DishNotFoundException} by returning a '404 Not Found' response.
     *
     * @param req The HTTP request that caused the exception.
     * @param e The {@link DishNotFoundException} that was thrown.
     * @return A message indicating that the dish was not found.
     */
    @ExceptionHandler(DishNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleDishNotFound(HttpServletRequest req, DishNotFoundException e) {
        LOGGER.error(e.getMessage() + ". Request: " + req.getServletPath(), e);
        return "Dish not found";
    }

    /**
     * Handles {@link InvalidDishNameException} by returning a '400 Bad Request' response.
     *
     * @param req The HTTP request that caused the exception.
     * @param e The {@link InvalidDishNameException} that was thrown.
     * @return A message indicating why the dish name was invalid.
     */
    @ExceptionHandler(InvalidDishNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidDishName(HttpServletRequest req, InvalidDishNameException e) {
        LOGGER.error(e.getMessage() + ". Request: " + req.getServletPath(), e);
        return e.getMessage();
    }

    /**
     * Handles all exceptions that are not specifically caught by other handlers,
     * returning a '500 Internal Server Error' response.
     *
     * @param req The HTTP request that caused the exception.
     * @param e The {@link Exception} that was thrown.
     * @return A message indicating that an internal server error occurred.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(HttpServletRequest req, Exception e) {
        LOGGER.error("An error occurred while processing the request. Request: " + req.getServletPath(), e);
        return "Internal server error";
    }
}
