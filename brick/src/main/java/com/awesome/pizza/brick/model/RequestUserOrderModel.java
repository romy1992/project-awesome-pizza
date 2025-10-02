package com.awesome.pizza.brick.model;

import com.awesome.pizza.commons.model.OrderPizzaModel;
import com.awesome.pizza.commons.model.UserModel;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestUserOrderModel {
  @NotNull private UserModel user;
  @NotNull private List<OrderPizzaModel> pizzas;
}
