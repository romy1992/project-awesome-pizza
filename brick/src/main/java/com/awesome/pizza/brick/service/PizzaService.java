package com.awesome.pizza.brick.service;

import com.awesome.pizza.brick.entity.Pizza;
import com.awesome.pizza.brick.mapper.PizzaMapper;
import com.awesome.pizza.brick.repository.PizzaRepository;
import com.awesome.pizza.commons.model.IngredientModel;
import com.awesome.pizza.commons.model.PizzaModel;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Service for managing pizzas in Awesome Pizza. */
@Slf4j
@Service
@RequiredArgsConstructor
public class PizzaService {
  private final PizzaRepository pizzaRepository;
  private final PizzaMapper pizzaMapper;
  private final IngredientService ingredientService;

  /**
   * Creates a new pizza and returns the created model.
   *
   * @param model pizza data to create
   * @return created pizza
   */
  @Transactional
  public PizzaModel createPizza(PizzaModel model) {
    log.info("<<< PizzaService.createPizza called with model: {} >>>", model);
    // Retrieve Ingredient entities from the database for each requested ingredient
    List<IngredientModel> ingredients =
        model.getIngredients().stream()
            .map(i -> ingredientService.addIngredientIfNotExists(i.getName()))
            .toList();
    model.setIngredients(ingredients);
    Pizza saved = pizzaRepository.saveAndFlush(pizzaMapper.toEntity(model));
    log.info("<<< Pizza created with id: {} >>>", saved.getId());
    return pizzaMapper.toModel(saved);
  }

  /**
   * Returns the list of all pizzas.
   *
   * @return list of pizzas
   */
  public List<PizzaModel> getAllPizzas() {
    log.info("<<< PizzaService.getAllPizzas called >>>");
    List<Pizza> pizzas = pizzaRepository.findAll();
    log.info("<<< Found {} pizzas >>>", pizzas.size());
    return pizzas.stream().map(pizzaMapper::toModel).toList();
  }

  /**
   * Returns the list of pizzas matching the provided IDs.
   *
   * @param ids list of pizza IDs
   * @return list of found pizzas
   */
  public List<PizzaModel> getPizzasByIds(List<Long> ids) {
    log.info("<<< PizzaService.getPizzasByIds called with ids: {} >>>", ids);
    return pizzaRepository.findAllById(ids).stream().map(pizzaMapper::toModel).toList();
  }

  /**
   * Updates an existing pizza.
   *
   * @param model new pizza model
   * @return updated pizza
   */
  @Transactional
  public PizzaModel updatePizza(PizzaModel model) {
    return pizzaMapper.toModel(pizzaRepository.saveAndFlush(pizzaMapper.toEntity(model)));
  }

  /**
   * Deletes a pizza by ID.
   *
   * @param id pizza ID
   * @return true if deleted, false otherwise
   */
  public boolean deletePizza(Long id) {
    log.info("<<< PizzaService.deletePizza called with id: {} >>>", id);
    if (pizzaRepository.existsById(id)) {
      pizzaRepository.deleteById(id);
      log.info("<<< Pizza with id: {} deleted >>>", id);
      return true;
    }
    log.warn("<<< Attempt to delete non-existing pizza with id: {} >>>", id);
    return false;
  }
}
