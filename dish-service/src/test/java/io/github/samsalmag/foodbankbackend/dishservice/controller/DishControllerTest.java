package io.github.samsalmag.foodbankbackend.dishservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.samsalmag.foodbankbackend.dishservice.dto.DishRequestDTO;
import io.github.samsalmag.foodbankbackend.dishservice.model.Dish;
import io.github.samsalmag.foodbankbackend.dishservice.model.Ingredient;
import io.github.samsalmag.foodbankbackend.dishservice.repository.DishRepository;
import io.github.samsalmag.foodbankbackend.dishservice.util.DishUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class DishControllerTest {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.18");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DishRepository dishRepository;

	@DynamicPropertySource
	static void mongodbProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@BeforeEach
	public void setUp() {
		dishRepository.deleteAll();		// Clear the repository before each test to avoid data conflicts
	}

	private DishRequestDTO getDishRequest() {
		return new DishRequestDTO(
				"Taco",
				List.of("Mexican", "Beef"),
				List.of(
						new Ingredient("Salsa", 4, "dl"),
						new Ingredient("Taco shell", 10, "pc"),
						new Ingredient("Beef meat", 1, "kg")),
				"Just make the taco");
	}

	@Test
	public void getAllDishes_shouldRespond200AndFetchNone() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dish/all")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(0));
	}

	@Test
	public void getAllDishes_shouldRespond200AndFetch1Dish() throws Exception {
		DishRequestDTO dishRequest = getDishRequest();
		Dish dish = DishUtil.toDishEntity(dishRequest);
		dishRepository.save(dish);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dish/all")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	public void getAllDishes_shouldRespond200AndFetch2Dishes() throws Exception {
		for (int i = 0; i < 2; i++) {
			DishRequestDTO dishRequest = getDishRequest();
			Dish dish = DishUtil.toDishEntity(dishRequest);
			dishRepository.save(dish);
		}

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dish/all")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(2));
	}

	@Test
	public void getDishByName_shouldRespond200() throws Exception {
		DishRequestDTO dishRequest = getDishRequest();
		Dish dish = DishUtil.toDishEntity(dishRequest);
		dishRepository.save(dish);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dish/name/Taco")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Taco"));
	}

	@Test
	public void getDishById_shouldRespond200() throws Exception {
		DishRequestDTO dishRequest = getDishRequest();
		Dish dish = DishUtil.toDishEntity(dishRequest);
		dishRepository.save(dish);

		String dishId = dish.getId();
		mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/dish/id/%s", dishId))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Taco"));
	}

	@Test
	public void addDish_shouldRespond201() throws Exception {
		DishRequestDTO dishRequest = getDishRequest();
		String dishRequestString = objectMapper.writeValueAsString(dishRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/dish/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(dishRequestString))
				.andExpect(status().isCreated());
	}

	@Test
	public void updateDish_shouldUpdateDishName() throws Exception {
		DishRequestDTO dishRequest = getDishRequest();
		Dish dish = DishUtil.toDishEntity(dishRequest);
		dishRepository.save(dish);
		String dishId = dish.getId();

		// Assert original name
		Assertions.assertEquals(dishRequest.getName(), "Taco");

		// Update to new name
		dishRequest.setName("Updated Taco");
		String updatedDishRequestString = objectMapper.writeValueAsString(dishRequest);

		mockMvc.perform(MockMvcRequestBuilders.put(String.format("/api/v1/dish/%s", dishId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedDishRequestString))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated Taco"));
	}

	@Test
	public void updateDish_shouldUpdateDishCategories() throws Exception {
		DishRequestDTO dishRequest = getDishRequest();
		Dish dish = DishUtil.toDishEntity(dishRequest);
		dishRepository.save(dish);
		String dishId = dish.getId();

		// Assert original categories
		Assertions.assertEquals(dishRequest.getCategories().get(0), "Mexican");
		Assertions.assertEquals(dishRequest.getCategories().get(1), "Beef");

		// Update to new categories
		dishRequest.setCategories(List.of("Updated 1", "Updated 2", "Updated 3"));
		String updatedDishRequestString = objectMapper.writeValueAsString(dishRequest);

		mockMvc.perform(MockMvcRequestBuilders.put(String.format("/api/v1/dish/%s", dishId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedDishRequestString))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.categories[0]").value("Updated 1"))
				.andExpect(jsonPath("$.categories[1]").value("Updated 2"))
				.andExpect(jsonPath("$.categories[2]").value("Updated 3"));
	}

	@Test
	public void updateDish_shouldUpdateIngredientNames() throws Exception {
		DishRequestDTO dishRequest = getDishRequest();
		Dish dish = DishUtil.toDishEntity(dishRequest);
		dishRepository.save(dish);
		String dishId = dish.getId();

		// Assert 3 original ingredients
		Assertions.assertEquals(dishRequest.getIngredients().get(0).getName(), "Salsa");
		Assertions.assertEquals(dishRequest.getIngredients().get(1).getName(), "Taco shell");
		Assertions.assertEquals(dishRequest.getIngredients().get(2).getName(), "Beef meat");

		// Change to 2 new ingredients
		dishRequest.setIngredients(List.of(
				new Ingredient("Number 1", 1, "Big"),
				new Ingredient("Number 2", 2, "Small")
		));
		String updatedDishRequestString = objectMapper.writeValueAsString(dishRequest);

		mockMvc.perform(MockMvcRequestBuilders.put(String.format("/api/v1/dish/%s", dishId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedDishRequestString))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ingredients[0].name").value("Number 1"))
				.andExpect(jsonPath("$.ingredients[1].name").value("Number 2"));
	}

	@Test
	public void updateDish_shouldUpdateIngredientQuantities() throws Exception {
		DishRequestDTO dishRequest = getDishRequest();
		Dish dish = DishUtil.toDishEntity(dishRequest);
		dishRepository.save(dish);
		String dishId = dish.getId();

		// Assert 3 original ingredients
		Assertions.assertEquals(dishRequest.getIngredients().get(0).getQuantity(), 4);
		Assertions.assertEquals(dishRequest.getIngredients().get(1).getQuantity(), 10);
		Assertions.assertEquals(dishRequest.getIngredients().get(2).getQuantity(), 1);

		// Change to 2 new ingredients
		dishRequest.setIngredients(List.of(
				new Ingredient("Number 1", 1, "Big"),
				new Ingredient("Number 2", 2, "Small")
		));
		String updatedDishRequestString = objectMapper.writeValueAsString(dishRequest);

		mockMvc.perform(MockMvcRequestBuilders.put(String.format("/api/v1/dish/%s", dishId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedDishRequestString))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ingredients[0].quantity").value(1))
				.andExpect(jsonPath("$.ingredients[1].quantity").value(2));
	}

	@Test
	public void updateDish_shouldUpdateIngredientUnits() throws Exception {
		DishRequestDTO dishRequest = getDishRequest();
		Dish dish = DishUtil.toDishEntity(dishRequest);
		dishRepository.save(dish);
		String dishId = dish.getId();

		// Assert 3 original ingredients
		Assertions.assertEquals(dishRequest.getIngredients().get(0).getUnit(), "dl");
		Assertions.assertEquals(dishRequest.getIngredients().get(1).getUnit(), "pc");
		Assertions.assertEquals(dishRequest.getIngredients().get(2).getUnit(), "kg");

		// Change to 2 new ingredients
		dishRequest.setIngredients(List.of(
				new Ingredient("Number 1", 1, "Big"),
				new Ingredient("Number 2", 2, "Small")
		));
		String updatedDishRequestString = objectMapper.writeValueAsString(dishRequest);

		mockMvc.perform(MockMvcRequestBuilders.put(String.format("/api/v1/dish/%s", dishId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedDishRequestString))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ingredients[0].unit").value("Big"))
				.andExpect(jsonPath("$.ingredients[1].unit").value("Small"));
	}

	@Test
	public void updateDish_shouldUpdateNumberOfIngredients() throws Exception {
		DishRequestDTO dishRequest = getDishRequest();
		Dish dish = DishUtil.toDishEntity(dishRequest);
		dishRepository.save(dish);
		String dishId = dish.getId();

		// Assert 3 original ingredients
		Assertions.assertEquals(dishRequest.getIngredients().size(), 3);

		// Change to 2 new ingredients
		dishRequest.setIngredients(List.of(
				new Ingredient("Number 1", 1, "Big"),
				new Ingredient("Number 2", 2, "Small")
		));
		String updatedDishRequestString = objectMapper.writeValueAsString(dishRequest);

		mockMvc.perform(MockMvcRequestBuilders.put(String.format("/api/v1/dish/%s", dishId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedDishRequestString))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ingredients.length()").value(2));
	}

	@Test
	public void updateDish_shouldUpdateDishInstructions() throws Exception {
		DishRequestDTO dishRequest = getDishRequest();
		Dish dish = DishUtil.toDishEntity(dishRequest);
		dishRepository.save(dish);
		String dishId = dish.getId();

		// Assert original instructions
		Assertions.assertEquals(dishRequest.getInstructions(), "Just make the taco");

		// Update to new instructions
		dishRequest.setName("Updated instructions");
		String updatedDishRequestString = objectMapper.writeValueAsString(dishRequest);

		mockMvc.perform(MockMvcRequestBuilders.put(String.format("/api/v1/dish/%s", dishId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedDishRequestString))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated instructions"));
	}

	@Test
	public void deleteDishById_shouldRespond200() throws Exception {
		DishRequestDTO dishRequest = getDishRequest();
		Dish dish = DishUtil.toDishEntity(dishRequest);
		dishRepository.save(dish);
		String dishId = dish.getId();

		mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/v1/dish/%s", dishId))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void deleteDishById_shouldRespond404whenIdNotExists() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/v1/dish/%s", "this dish id does not exist"))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}
