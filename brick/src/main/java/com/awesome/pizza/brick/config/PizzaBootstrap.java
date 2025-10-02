package com.awesome.pizza.brick.config;

import com.awesome.pizza.brick.service.PizzaService;
import com.awesome.pizza.commons.model.IngredientModel;
import com.awesome.pizza.commons.model.PizzaModel;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PizzaBootstrap implements CommandLineRunner {
  private final PizzaService pizzaService;

  private static final List<PizzaData> DEFAULT_PIZZAS =
      Arrays.asList(
          new PizzaData(
              "Margherita",
              "La classica pizza italiana con pomodoro, mozzarella e basilico fresco.",
              Arrays.asList("Pomodoro", "Mozzarella", "Basilico"),
              BigDecimal.valueOf(7.50)),
          new PizzaData(
              "Diavola",
              "Pizza piccante con salame, pomodoro e mozzarella.",
              Arrays.asList("Pomodoro", "Mozzarella", "Salame piccante"),
              BigDecimal.valueOf(8.00)),
          new PizzaData(
              "Prosciutto e Funghi",
              "Pizza con prosciutto cotto, funghi, pomodoro e mozzarella.",
              Arrays.asList("Pomodoro", "Mozzarella", "Prosciutto", "Funghi"),
              BigDecimal.valueOf(8.50)),
          new PizzaData(
              "Quattro Stagioni",
              "Pizza ricca con carciofi, funghi, prosciutto e olive.",
              Arrays.asList("Pomodoro", "Mozzarella", "Carciofi", "Funghi", "Prosciutto", "Olive"),
              BigDecimal.valueOf(9.00)),
          new PizzaData(
              "Capricciosa",
              "Pizza con carciofi, funghi, prosciutto e uovo.",
              Arrays.asList("Pomodoro", "Mozzarella", "Carciofi", "Funghi", "Prosciutto", "Uovo"),
              BigDecimal.valueOf(9.00)),
          new PizzaData(
              "Quattro Formaggi",
              "Pizza con mozzarella, gorgonzola, fontina e parmigiano.",
              Arrays.asList("Mozzarella", "Gorgonzola", "Fontina", "Parmigiano"),
              BigDecimal.valueOf(9.50)),
          new PizzaData(
              "Vegetariana",
              "Pizza con verdure grigliate, pomodoro e mozzarella.",
              Arrays.asList("Pomodoro", "Mozzarella", "Verdure grigliate"),
              BigDecimal.valueOf(8.50)),
          new PizzaData(
              "Tonno e Cipolla",
              "Pizza con tonno, cipolla, pomodoro e mozzarella.",
              Arrays.asList("Pomodoro", "Mozzarella", "Tonno", "Cipolla"),
              BigDecimal.valueOf(8.50)),
          new PizzaData(
              "Bufalina",
              "Pizza con mozzarella di bufala, pomodoro e basilico.",
              Arrays.asList("Pomodoro", "Mozzarella di bufala", "Basilico"),
              BigDecimal.valueOf(9.00)),
          new PizzaData(
              "Salsiccia e Friarielli",
              "Pizza tipica napoletana con salsiccia e friarielli.",
              Arrays.asList("Mozzarella", "Salsiccia", "Friarielli"),
              BigDecimal.valueOf(9.50)));

  @Transactional
  @Override
  public void run(String... args) {
    List<PizzaModel> existingPizzas = pizzaService.getAllPizzas();
    if (existingPizzas.size() < 10) {
      int toCreate = 10 - existingPizzas.size();
      DEFAULT_PIZZAS.stream()
          .limit(toCreate)
          .forEach(
              pizzaData -> {
                PizzaModel pizzaModel =
                    PizzaModel.builder()
                        .name(pizzaData.name)
                        .description(pizzaData.description)
                        .price(pizzaData.price)
                        .ingredients(createIngredientModels(pizzaData.ingredients))
                        .build();
                pizzaService.createPizza(pizzaModel);
              });
    }
  }

  private List<IngredientModel> createIngredientModels(List<String> ingredientNames) {
    return ingredientNames.stream()
        .map(ingredient -> IngredientModel.builder().name(ingredient).build())
        .toList();
  }

  private static class PizzaData {
    String name;
    String description;
    List<String> ingredients;
    BigDecimal price;

    PizzaData(String name, String description, List<String> ingredients, BigDecimal price) {
      this.name = name;
      this.description = description;
      this.ingredients = ingredients;
      this.price = price;
    }
  }
}
