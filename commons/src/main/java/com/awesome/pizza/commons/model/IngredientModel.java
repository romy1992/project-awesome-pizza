package com.awesome.pizza.commons.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientModel {
  private Long id;
  @NotNull private String name;
}
