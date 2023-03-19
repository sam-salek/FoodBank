package com.samsalek.foodbank.dish;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends MongoRepository<Dish, String> {

    List<Dish> findByName(String name);
    Long deleteByName(String name);
    boolean existsByName(String name);
}
