package com.awesome.pizza.brick.controller;

import com.awesome.pizza.brick.service.PizzaService;
import com.awesome.pizza.commons.model.PizzaModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing pizzas in Awesome Pizza.
 */
@Slf4j
@RestController
@RequestMapping("/api/pizzas")
@RequiredArgsConstructor
public class PizzaController {
    private final PizzaService pizzaService;

    /**
     * Creates a new pizza.
     * @param model the pizza model to create
     * @return the created pizza model
     */
    @PostMapping
    public ResponseEntity<PizzaModel> createPizza(@RequestBody PizzaModel model) {
        log.info("<<< POST /api/pizzas - createPizza called with model: {} >>>", model);
        return ResponseEntity.ok(pizzaService.createPizza(model));
    }

    /**
     * Returns a list of all pizzas.
     * @return list of pizza models
     */
    @GetMapping
    public ResponseEntity<List<PizzaModel>> getAllPizzas() {
        log.info("<<< GET /api/pizzas - getAllPizzas called >>>");
        return ResponseEntity.ok(pizzaService.getAllPizzas());
    }

    /**
     * Returns pizza details by ID.
     * @param id the pizza identifier
     * @return pizza model, if present
     */
    @GetMapping("/{id}")
    public ResponseEntity<PizzaModel> getPizzaById(@PathVariable Long id) {
        log.info("<<< GET /api/pizzas/{} - getPizzaById called >>>", id);
        return pizzaService.getPizzaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing pizza.
     * @param id the pizza identifier
     * @param model the new pizza model
     * @return updated pizza model, if present
     */
    @PutMapping("/{id}")
    public ResponseEntity<PizzaModel> updatePizza(@PathVariable Long id, @RequestBody PizzaModel model) {
        log.info("<<< PUT /api/pizzas/{} - updatePizza called with model: {} >>>", id, model);
        return pizzaService.updatePizza(id, model)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a pizza by ID.
     * @param id the pizza identifier
     * @return no content if deleted, not found otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizza(@PathVariable Long id) {
        log.info("<<< DELETE /api/pizzas/{} - deletePizza called >>>", id);
        return pizzaService.deletePizza(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
