package io.github.samsalmag.foodbankbackend.dishservice.repository;

import io.github.samsalmag.foodbankbackend.dishservice.model.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends MongoRepository<Dish, String> {

    List<Dish> findByName(String name);
    boolean existsByName(String name);
}
