package com.samsalek.foodbank;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Document
public class Dish {
    @Id
    private String id;

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
