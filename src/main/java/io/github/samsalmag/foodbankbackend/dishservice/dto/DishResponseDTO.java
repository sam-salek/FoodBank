package io.github.samsalmag.foodbankbackend.dishservice.dto;

import io.github.samsalmag.foodbankbackend.dishservice.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishResponseDTO {

    private String id;
    private String name;
    private List<String> categories;
    private List<Ingredient> ingredients;
    private String instructions;
    private LocalDateTime creationTime;
}
