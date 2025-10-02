package com.awesome.pizza.brick.entity;

import com.awesome.pizza.commons.model.OrderStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Entity representing a customer order in Awesome Pizza. Each order contains a list of pizzas and a
 * status.
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Unique code for tracking the order */
  @Column(name = "CODE", unique = true, nullable = false, length = 50)
  private String code;

  /** Current status of the order */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false)
  private OrderStatus status;

  /** List of ordered pizzas (OrderPizza) */
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "order_id")
  private List<OrderPizza> orderedPizzas;

  /** Timestamp when the order was created */
  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  /** User associated with the order (one-to-one) */
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private User user;

  /** Total price of the order */
  private BigDecimal totalPrice;
}
