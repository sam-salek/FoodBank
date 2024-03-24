package com.samsalek.foodbankbackend;

import com.samsalek.foodbankbackend.dish.Dish;

public class DishNotFoundException extends RuntimeException {

    private static final String BASE_MESSAGE = "Unable to find dish";

    public DishNotFoundException() {
        super(BASE_MESSAGE);
    }

    public DishNotFoundException(Dish dish) {
        super(String.format("%s with id %s and name %s", BASE_MESSAGE, dish.getId(), dish.getName()));
    }

    public DishNotFoundException(String dishId) {
        super(String.format("%s with id %s", BASE_MESSAGE, dishId));
    }
}
