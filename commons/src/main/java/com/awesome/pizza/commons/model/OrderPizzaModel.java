package com.awesome.pizza.commons.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Model representing a pizza ordered within an order (DTO for OrderPizza entity). Stores the
 * description and price of the pizza at the time of the order.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPizzaModel {
  private Long idOrderPizza;
  @NotNull private Long pizzaId;
  private String descriptionUser;
  //
  private String namePizza;
}
