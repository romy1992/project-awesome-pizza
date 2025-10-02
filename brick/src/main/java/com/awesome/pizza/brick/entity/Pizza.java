package com.awesome.pizza.brick.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;

/** Entity representing a pizza in Awesome Pizza. */
@Entity
@Table(name = "pizzas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pizza {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Name of the pizza */
  @Column(name = "NAME", nullable = false, unique = true)
  private String name;

  /** Description of the pizza */
  @Column(name = "DESCRIPTION", length = 1000)
  private String description;

  /** List of ingredients */
  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinTable(
      name = "pizza_ingredient",
      joinColumns = @JoinColumn(name = "pizza_id"),
      inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
  private List<Ingredient> ingredients;

  /** Price of the pizza */
  @Column(name = "PRICE", nullable = false)
  private BigDecimal price;
}
