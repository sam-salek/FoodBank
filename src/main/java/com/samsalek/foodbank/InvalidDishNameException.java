package com.samsalek.foodbank;

public class InvalidDishNameException extends IllegalArgumentException {

    public InvalidDishNameException() {
        super("Dish name is invalid");
    }

    public InvalidDishNameException(String message) {
        super(message);
    }

    public static InvalidDishNameException nullDishName() {
        return new InvalidDishNameException("Dish name cannot be null");
    }

    public static InvalidDishNameException emptyDishName() {
        return new InvalidDishNameException("Dish name cannot be empty");
    }

    public static InvalidDishNameException duplicateDishName() {
        return new InvalidDishNameException("Dish with this name already exists");
    }
}
