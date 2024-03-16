package com.samsalek.foodbank.dish;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if (dishes.isEmpty()) throw new IllegalArgumentException("No dishes with name: " + name);
        return dishes.get(0);
    }

    public Dish getDishById(String id) {
        Optional<Dish> dish = dishRepository.findById(id);
        if (dish.isEmpty()) throw new IllegalArgumentException("No dishes with id: " + id);
        return dish.get();
    }

    public Dish addDish(Dish dish) {
        // Name check
        if (dish.getName() == null) throw new IllegalArgumentException("Dish name cannot be null!");
        if (dish.getName().length() == 0) throw new IllegalArgumentException("Dish name cannot be of length 0!");
        if (dishRepository.existsByName(dish.getName())) throw new IllegalArgumentException("Dish with name \"" + dish.getName() + "\" already exists!");

        return dishRepository.save(dish);
    }

    public void deleteDishById(String id) {
        // Id check
        if (dishRepository.findById(id).isEmpty()) throw new IllegalArgumentException("No dishes with id: " + id);

        dishRepository.deleteById(id);
    }

    public Dish updateDish(String id, Dish dish) {
        // Id check
        if (dishRepository.findById(id).isEmpty()) throw new IllegalArgumentException("No dishes with id: " + id);

        // Name check
        if (dish.getName() == null) throw new IllegalArgumentException("Dish name cannot be null!");
        if (dish.getName().length() == 0) throw new IllegalArgumentException("Dish name cannot be of length 0!");
        if (dishRepository.existsByName(dish.getName())) throw new IllegalArgumentException("Dish with name \"" + dish.getName() + "\" already exists!");

       dishRepository.deleteById(id);
       dish.setId(id);
       return dishRepository.save(dish);
    }
}
