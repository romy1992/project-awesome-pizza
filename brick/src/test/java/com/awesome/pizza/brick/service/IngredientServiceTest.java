package com.awesome.pizza.brick.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.awesome.pizza.brick.entity.Ingredient;
import com.awesome.pizza.brick.mapper.IngredientMapper;
import com.awesome.pizza.brick.repository.IngredientRepository;
import com.awesome.pizza.commons.model.IngredientModel;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {
  @Mock private IngredientRepository ingredientRepository;
  @Mock private IngredientMapper ingredientMapper;
  @InjectMocks private IngredientService ingredientService;

  @Test
  void addIngredientIfNotExists_shouldAddNewIngredient() {
    String name = "Mozzarella";
    Ingredient ingredient = Ingredient.builder().name(name.toLowerCase()).build();
    IngredientModel model = new IngredientModel();
    when(ingredientRepository.findByName(name.toLowerCase())).thenReturn(Optional.empty());
    when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);
    when(ingredientMapper.toModel(any(Ingredient.class))).thenReturn(model);
    IngredientModel result = ingredientService.addIngredientIfNotExists(name);
    assertThat(result).isNotNull();
  }

  @Test
  void addIngredientIfNotExists_shouldReturnExistingIngredient() {
    String name = "Pomodoro";
    Ingredient ingredient = Ingredient.builder().name(name.toLowerCase()).build();
    IngredientModel model = new IngredientModel();
    when(ingredientRepository.findByName(name.toLowerCase())).thenReturn(Optional.of(ingredient));
    when(ingredientMapper.toModel(ingredient)).thenReturn(model);
    IngredientModel result = ingredientService.addIngredientIfNotExists(name);
    assertThat(result).isNotNull();
  }
}
