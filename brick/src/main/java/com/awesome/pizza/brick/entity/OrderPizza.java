package com.awesome.pizza.brick.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a pizza ordered within an order. Stores the description and price of the
 * pizza at the time of the order.
 */
@Entity
@Table(name = "order_pizza")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "pizza")
public class OrderPizza {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idOrderPizza;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pizza_id", nullable = false)
  private Pizza pizza;

  @Column(length = 1000)
  private String descriptionUser;
}
