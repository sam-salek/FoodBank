package io.github.samsalmag.foodbankbackend.dishservice.service;

import io.github.samsalmag.foodbankbackend.dishservice.dto.DishRequestDTO;
import io.github.samsalmag.foodbankbackend.dishservice.dto.DishResponseDTO;
import io.github.samsalmag.foodbankbackend.dishservice.exception.DishNotFoundException;
import io.github.samsalmag.foodbankbackend.dishservice.exception.InvalidDishNameException;
import io.github.samsalmag.foodbankbackend.dishservice.repository.DishRepository;
import io.github.samsalmag.foodbankbackend.dishservice.model.Dish;
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

    public List<DishResponseDTO> getAllDishes() {
        return dishRepository.findAll().stream().map(this::toDishResponseDto).toList();
    }

    public DishResponseDTO getDishByName(String name) {
        List<Dish> dishes = dishRepository.findByName(name);
        if (dishes.isEmpty()) throw new DishNotFoundException();
        return dishes.stream().map(this::toDishResponseDto).toList().get(0);
    }

    public DishResponseDTO getDishById(String id) {
        Optional<Dish> dish = dishRepository.findById(id);
        if (dish.isEmpty()) throw new DishNotFoundException(id);
        return this.toDishResponseDto(dish.get());
    }

    public DishResponseDTO addDish(DishRequestDTO dishRequest) {
        // Name check
        if (dishRequest.getName() == null) throw InvalidDishNameException.nullDishName();
        if (dishRequest.getName().length() == 0) throw InvalidDishNameException.emptyDishName();
        if (dishRepository.existsByName(dishRequest.getName())) throw InvalidDishNameException.duplicateDishName();

        Dish newDish = this.toDishEntity(dishRequest);

        Dish savedDish = dishRepository.save(newDish);
        return this.toDishResponseDto(savedDish);
    }

    public void deleteDishById(String id) {
        // Id check
        if (dishRepository.findById(id).isEmpty()) throw new DishNotFoundException(id);

        dishRepository.deleteById(id);
    }

    /**
     * Updates the dish of the given id, by replacing its data with the data of a new Dish.
     *
     * @param id            ID of the dish to be updated.
     * @param dishRequest   Dish request whose data will replace the old dish's data.
     * @return              The newly updated dish.
     */
    public DishResponseDTO updateDish(String id, DishRequestDTO dishRequest) {
        // Id check
        if (dishRepository.findById(id).isEmpty()) throw new DishNotFoundException(id);

        // Name check
        if (dishRequest.getName() == null) throw InvalidDishNameException.nullDishName();
        if (dishRequest.getName().length() == 0) throw InvalidDishNameException.emptyDishName();

        // Delete dish with the given id
        // Create new dish that will replace the deleted dish (by using same id as deleted dish)
        dishRepository.deleteById(id);
        Dish newDish = this.toDishEntity(dishRequest);
        newDish.setId(id);

        Dish savedDish = dishRepository.save(newDish);
        return this.toDishResponseDto(savedDish);
    }

    private DishResponseDTO toDishResponseDto(Dish dish) {
        return new DishResponseDTO(dish.getId(),
                                    dish.getName(),
                                    dish.getCategories(),
                                    dish.getIngredients(),
                                    dish.getInstructions(),
                                    dish.getCreationTime());
    }

    private Dish toDishEntity(DishRequestDTO dishRequest) {
        Dish newDish = new Dish();
        newDish.setName(dishRequest.getName());
        newDish.setCategories(dishRequest.getCategories());
        newDish.setIngredients(dishRequest.getIngredients());
        newDish.setInstructions(dishRequest.getInstructions());
        return newDish;
    }
}
