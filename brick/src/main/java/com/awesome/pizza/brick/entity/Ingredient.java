package com.awesome.pizza.brick.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "NAME", unique = true, nullable = false)
  private String name;

  @ManyToMany(mappedBy = "ingredients")
  private Set<Pizza> pizzas;
}
