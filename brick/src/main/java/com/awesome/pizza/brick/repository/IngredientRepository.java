package com.awesome.pizza.brick.repository;

import com.awesome.pizza.brick.entity.Ingredient;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository for managing Ingredient entities in Awesome Pizza. */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
  Optional<Ingredient> findByName(String name);
}
