package io.github.samsalmag.foodbankbackend.dishservice.dto;

import io.github.samsalmag.foodbankbackend.dishservice.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishRequestDTO {

    private String name;
    private List<String> categories;
    private List<Ingredient> ingredients;
    private String instructions;
}
