package com.samsalek.foodbank.dish;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "dishes")
@NoArgsConstructor
public class Dish {
    @Id
    private String id;

    private String name;
    private String category = "Unknown";
    private List<String> ingredients = new ArrayList<>();
    private List<String> instructions = new ArrayList<>();
    private LocalDateTime creationTime = LocalDateTime.now();

    public Dish(String name) {
        this.name = name;
    }

    public Dish(String name, String category, List<String> ingredients, List<String> instructions) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }
}
