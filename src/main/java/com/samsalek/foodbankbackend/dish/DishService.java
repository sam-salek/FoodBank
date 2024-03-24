package com.samsalek.foodbankbackend.dish;

import com.samsalek.foodbankbackend.DishNotFoundException;
import com.samsalek.foodbankbackend.InvalidDishNameException;
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
        if (dishes.isEmpty()) throw new DishNotFoundException();
        return dishes.get(0);
    }

    public Dish getDishById(String id) {
        Optional<Dish> dish = dishRepository.findById(id);
        if (dish.isEmpty()) throw new DishNotFoundException(id);
        return dish.get();
    }

    public Dish addDish(Dish dish) {
        // Name check
        if (dish.getName() == null) throw InvalidDishNameException.nullDishName();
        if (dish.getName().length() == 0) throw InvalidDishNameException.emptyDishName();
        if (dishRepository.existsByName(dish.getName())) throw InvalidDishNameException.duplicateDishName();

        return dishRepository.save(dish);
    }

    public void deleteDishById(String id) {
        // Id check
        if (dishRepository.findById(id).isEmpty()) throw new DishNotFoundException(id);

        dishRepository.deleteById(id);
    }

    public Dish updateDish(String id, Dish dish) {
        // Id check
        if (dishRepository.findById(id).isEmpty()) throw new DishNotFoundException(id);

        // Name check
        if (dish.getName() == null) throw InvalidDishNameException.nullDishName();
        if (dish.getName().length() == 0) throw InvalidDishNameException.emptyDishName();
        if (dishRepository.existsByName(dish.getName())) throw InvalidDishNameException.duplicateDishName();

       dishRepository.deleteById(id);
       dish.setId(id);
       return dishRepository.save(dish);
    }
}
