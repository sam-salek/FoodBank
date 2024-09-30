package io.github.samsalmag.foodbankbackend.dishservice.service;

import io.github.samsalmag.foodbankbackend.dishservice.dto.DishRequestDTO;
import io.github.samsalmag.foodbankbackend.dishservice.dto.DishResponseDTO;
import io.github.samsalmag.foodbankbackend.dishservice.exception.DishNotFoundException;
import io.github.samsalmag.foodbankbackend.dishservice.exception.InvalidDishNameException;
import io.github.samsalmag.foodbankbackend.dishservice.repository.DishRepository;
import io.github.samsalmag.foodbankbackend.dishservice.model.Dish;
import io.github.samsalmag.foodbankbackend.dishservice.util.DishUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DishService {

    @Autowired
    private final DishRepository dishRepository;

    public List<DishResponseDTO> getAllDishes() {
        return dishRepository.findAll().stream().map(DishUtil::toDishResponseDto).toList();
    }

    public DishResponseDTO getDishByName(String name) {
        List<Dish> dishes = dishRepository.findByName(name);
        if (dishes.isEmpty()) throw new DishNotFoundException();
        return dishes.stream().map(DishUtil::toDishResponseDto).toList().get(0);
    }

    public DishResponseDTO getDishById(String id) {
        Optional<Dish> dish = dishRepository.findById(id);
        if (dish.isEmpty()) throw new DishNotFoundException(id);
        return DishUtil.toDishResponseDto(dish.get());
    }

    public DishResponseDTO addDish(DishRequestDTO dishRequest) {
        // Name check
        if (dishRequest.getName() == null) throw InvalidDishNameException.nullDishName();
        if (dishRequest.getName().length() == 0) throw InvalidDishNameException.emptyDishName();
        if (dishRepository.existsByName(dishRequest.getName())) throw InvalidDishNameException.duplicateDishName();

        Dish newDish = DishUtil.toDishEntity(dishRequest);

        Dish savedDish = dishRepository.save(newDish);
        return DishUtil.toDishResponseDto(savedDish);
    }

    public void deleteDishById(String id) {
        // Id check
        if (dishRepository.findById(id).isEmpty()) throw new DishNotFoundException(id);

        dishRepository.deleteById(id);
    }

    /**
     * Updates the dish of the given id, by replacing its data with the data of a new Dish.
     *
     * @param id ID of the dish to be updated.
     * @param dishRequest Dish request whose data will replace the old dish's data.
     * @return The newly updated dish.
     */
    public DishResponseDTO updateDish(String id, DishRequestDTO dishRequest) {
        // Id check
        Optional<Dish> dishToUpdate = dishRepository.findById(id);
        if (dishToUpdate.isEmpty()) throw new DishNotFoundException(id);

        // Name check
        if (dishRequest.getName() == null) throw InvalidDishNameException.nullDishName();
        if (dishRequest.getName().length() == 0) throw InvalidDishNameException.emptyDishName();

        // If dish with requested name already exists...
        // Then check if it belongs to the dish being modified (ok scenario). If it belongs to another dish, then throw exception
        if (dishRepository.existsByName(dishRequest.getName())) {
            if (!dishToUpdate.get().getName().equals(dishRequest.getName())) throw InvalidDishNameException.duplicateDishName();
        }

        // Delete dish with the given id
        // Create new dish that will replace the deleted dish (by using same id as deleted dish)
        dishRepository.deleteById(id);
        Dish newDish = DishUtil.toDishEntity(dishRequest);
        newDish.setId(id);
        newDish.setCreationTime(dishToUpdate.get().getCreationTime());
        newDish.setLatestModTime(LocalDateTime.now());

        Dish savedDish = dishRepository.save(newDish);
        return DishUtil.toDishResponseDto(savedDish);
    }
}
