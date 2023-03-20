package com.samsalek.foodbank.dish;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "dishes")
@NoArgsConstructor
public class Dish {
    @Id
    private String id;

    private String name;
    private String category;
    private List<String> ingredients;
    private LocalDateTime created;

    public Dish(String name) {
        this.name = name;
    }

    public Dish(String name, String category, List<String> ingredients, LocalDateTime created) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        this.created = created;
    }
}
