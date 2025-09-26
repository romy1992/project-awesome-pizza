package com.awesome.pizza.commons.model;

import lombok.*;

import java.util.List;

/** DTO for transferring pizza data via API. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PizzaModel {
  private Long id;
  private String name;
  private String description;
  private Double price;
  private List<String> ingredients;
}
