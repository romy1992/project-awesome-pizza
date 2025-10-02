package com.awesome.pizza.commons.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** DTO for transferring order data via API. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderModel {
  private Long id;
  private String code;
  private String status;
  @NotNull private List<OrderPizzaModel> orderedPizzas;
  private LocalDateTime createdAt;
  @NotNull private UserModel user;
  private BigDecimal totalPrice;
}
