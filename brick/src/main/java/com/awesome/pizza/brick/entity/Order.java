package com.awesome.pizza.brick.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a customer order in Awesome Pizza.
 * Each order contains a list of pizzas and a status.
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
    private String code;

    /** Current status of the order */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /** List of pizzas in the order */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "order_pizzas",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "pizza_id")
    )
    private List<Pizza> pizzas;

    /** Timestamp when the order was created */
    private LocalDateTime createdAt;

    /** User associated with the order (one-to-one) */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    /** True if the order is takeaway, false if pickup */
    private boolean isTakeaway;
}
