package com.samsalek.foodbank.dish;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DishService {

    @Autowired
    private final DishRepository dishRepository;

    public List<Dish> getDishes() {
        return dishRepository.findAll();
    }

    public Dish getDishByName(String name) {
        List<Dish> dishes = dishRepository.findByName(name);
        if (dishes.isEmpty()) throw new IllegalStateException("No dishes with name: " + name);
        return dishes.get(0);
    }

    public Dish addDish(Dish dish) {
        boolean exists = dishRepository.existsByName(dish.getName());
        if (exists) throw new IllegalStateException("Dish with name \"" + dish.getName() + "\" already exists!");
        return dishRepository.save(dish);
    }

    public Long deleteDishByName(String name) {
        boolean exists = dishRepository.existsByName(name);
        if (!exists) throw new IllegalStateException("No dishes with name: " + name);
        return dishRepository.deleteByName(name);
    }

    public Dish updateDish(String name, String newName, String newCategory, List<String> newIngredients) {
        List<Dish> dishes = dishRepository.findByName(name);
        if (dishes.isEmpty()) throw new IllegalStateException("No dishes with name: " + name);
        Dish dish = dishes.get(0);

        if (newName != null && newName.length() > 0 && !newName.equals(name)) {
            if (dishRepository.existsByName(newName)) throw new IllegalStateException("Dish with name \"" + newName + "\" already exists!");
            dish.setName(newName);
        }
        if (newCategory != null && newCategory.length() > 0 && !newCategory.equals(dish.getCategory())) dish.setCategory(newCategory);
        if (newIngredients != null && newIngredients.size() > 0) dish.setIngredients(newIngredients);

        return dishRepository.save(dish);
    }
}
