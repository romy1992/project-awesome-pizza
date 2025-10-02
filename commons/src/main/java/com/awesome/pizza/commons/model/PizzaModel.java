package com.awesome.pizza.commons.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;
import jakarta.validation.constraints.NotNull;

/** DTO for transferring pizza data via API. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PizzaModel {
  private Long id;
  @NotNull private String name;
  @NotNull private String description;
  @NotNull private BigDecimal price;
  @NotNull private List<IngredientModel> ingredients;
}
