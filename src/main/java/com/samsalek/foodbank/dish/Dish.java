package com.samsalek.foodbank.dish;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class Dish {
    @Id
    private String dishId;

    private String name;
    private String category;
    private List<String> ingredients;
    private LocalDateTime created;

    public Dish(String name, String category, List<String> ingredients, LocalDateTime created) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        this.created = created;
    }
}
