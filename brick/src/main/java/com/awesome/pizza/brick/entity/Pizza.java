package com.awesome.pizza.brick.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

/**
 * Entity representing a pizza in Awesome Pizza.
 */
@Entity
@Table(name = "pizzas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the pizza */
    private String name;

    /** List of ingredients */
    @ElementCollection
    private List<String> ingredients;

    /** Price of the pizza */
    private Double price;
}

