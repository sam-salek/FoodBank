package io.github.samsalmag.foodbankbackend.dishservice.util;

import io.github.samsalmag.foodbankbackend.dishservice.dto.DishRequestDTO;
import io.github.samsalmag.foodbankbackend.dishservice.dto.DishResponseDTO;
import io.github.samsalmag.foodbankbackend.dishservice.model.Dish;

public class DishUtil {

    // Private constructor. Utility classes should not be instantiated.
    private DishUtil(){}

    /**
     * Converts a {@link Dish} entity to a {@link DishResponseDTO} object.
     *
     * @param dish The entity object to convert.
     * @return A {@link DishResponseDTO} object, converted from {@code dish}.
     */
    public static DishResponseDTO toDishResponseDto(Dish dish) {
        return new DishResponseDTO(dish.getId(),
                dish.getName(),
                dish.getCategories(),
                dish.getIngredients(),
                dish.getInstructions(),
                dish.getCreationTime(),
                dish.getLatestModTime());
    }

    /**
     * Converts a {@link DishRequestDTO} object to a {@link Dish} entity object.
     *
     * @param dishRequest The object to convert.
     * @return A {@link Dish} entity object, converted from {@code dishRequest}.
     */
    public static Dish toDishEntity(DishRequestDTO dishRequest) {
        Dish newDish = new Dish();
        newDish.setName(dishRequest.getName());
        newDish.setCategories(dishRequest.getCategories());
        newDish.setIngredients(dishRequest.getIngredients());
        newDish.setInstructions(dishRequest.getInstructions());
        return newDish;
    }
}
