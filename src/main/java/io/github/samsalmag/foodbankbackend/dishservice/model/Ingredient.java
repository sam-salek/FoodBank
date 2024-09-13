package io.github.samsalmag.foodbankbackend.dishservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    private String name;
    private int amount;
    private String unit;
}
