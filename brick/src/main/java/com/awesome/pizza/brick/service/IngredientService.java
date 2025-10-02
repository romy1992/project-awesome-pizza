package com.awesome.pizza.brick.service;

import com.awesome.pizza.brick.entity.Ingredient;
import com.awesome.pizza.brick.mapper.IngredientMapper;
import com.awesome.pizza.brick.repository.IngredientRepository;
import com.awesome.pizza.commons.model.IngredientModel;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class IngredientService {
  private final IngredientRepository ingredientRepository;
  private final IngredientMapper ingredientMapper;

  /**
   * Adds a new ingredient if it does not already exist.
   *
   * @param name the ingredient name
   */
  public IngredientModel addIngredientIfNotExists(String name) {
    String normalized = name.trim().toLowerCase();
    Optional<Ingredient> ingredientOpt = ingredientRepository.findByName(normalized);
    if (ingredientOpt.isEmpty()) {
      log.info("<<< Ingredient '{}' added to the database >>>", normalized);
      return ingredientMapper.toModel(
          ingredientRepository.save(Ingredient.builder().name(normalized).build()));
    }
    log.info("<<< Ingredient '{}' already exists in the database >>>", normalized);
    return ingredientMapper.toModel(ingredientOpt.get());
  }
}
