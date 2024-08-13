package io.github.samsalmag.foodbankbackend.dish;

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
    private String category = "Unknown";
    private List<String> ingredients = new ArrayList<>();
    private List<String> instructions = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime creationTime = LocalDateTime.now();
}
