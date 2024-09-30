package io.github.samsalmag.foodbankbackend.dishservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Dish {
    @Id
    private String id;

    private String name;
    private List<String> categories = new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();
    private String instructions;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime creationTime = LocalDateTime.now();

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime latestModTime = creationTime;
}
