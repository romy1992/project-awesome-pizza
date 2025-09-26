package com.awesome.pizza.commons.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for transferring order data via API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderModel {
	private Long id;
	private String code;
	private String status;
	private List<PizzaModel> pizzas;
	private LocalDateTime createdAt;
	private boolean isTakeaway;
	private UserModel user;
}
