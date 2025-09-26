package com.awesome.pizza.brick.service;

import com.awesome.pizza.brick.entity.Pizza;
import com.awesome.pizza.brick.mapper.PizzaMapper;
import com.awesome.pizza.brick.repository.PizzaRepository;
import com.awesome.pizza.commons.model.PizzaModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing pizzas in Awesome Pizza.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PizzaService {
    private final PizzaRepository pizzaRepository;
    private final PizzaMapper pizzaMapper;

    /**
     * Creates a new pizza.
     * @param model the pizza model to create
     * @return the created pizza model
     */
    public PizzaModel createPizza(PizzaModel model) {
        log.info("<<< PizzaService.createPizza called with model: {} >>>", model);
        Pizza pizza = pizzaMapper.toEntity(model);
        Pizza saved = pizzaRepository.save(pizza);
        log.info("<<< Pizza created with id: {} >>>", saved.getId());
        return pizzaMapper.toModel(saved);
    }

    /**
     * Returns a list of all pizzas.
     * @return list of pizza models
     */
    public List<PizzaModel> getAllPizzas() {
        log.info("<<< PizzaService.getAllPizzas called >>>");
        List<Pizza> pizzas = pizzaRepository.findAll();
        log.info("<<< Found {} pizzas >>>", pizzas.size());
        return pizzas.stream()
                .map(pizzaMapper::toModel)
                .collect(Collectors.toList());
    }

    /**
     * Returns pizza details by ID.
     * @param id the pizza identifier
     * @return pizza model, if present
     */
    public Optional<PizzaModel> getPizzaById(Long id) {
        log.info("<<< PizzaService.getPizzaById called with id: {} >>>", id);
        return pizzaRepository.findById(id).map(pizzaMapper::toModel);
    }

    /**
     * Updates an existing pizza.
     * @param id the pizza identifier
     * @param model the new pizza model
     * @return updated pizza model, if present
     */
    public Optional<PizzaModel> updatePizza(Long id, PizzaModel model) {
        log.info("<<< PizzaService.updatePizza called with id: {} and model: {} >>>", id, model);
        return pizzaRepository.findById(id).map(existing -> {
            Pizza updated = pizzaMapper.toEntity(model);
            updated.setId(id);
            Pizza saved = pizzaRepository.save(updated);
            log.info("<<< Pizza updated with id: {} >>>", saved.getId());
            return pizzaMapper.toModel(saved);
        });
    }

    /**
     * Deletes a pizza by ID.
     * @param id the pizza identifier
     * @return true if the pizza was deleted, false otherwise
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
