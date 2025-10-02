package com.awesome.pizza.brick.controller;

import com.awesome.pizza.brick.service.PizzaService;
import com.awesome.pizza.commons.model.PizzaModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing pizzas in Awesome Pizza.
 * <p>
 * <b>All endpoints in this controller are intended for internal use by the pizzeria via the restaurant portal only.</b>
 */
@Slf4j
@RestController
@RequestMapping("/api/pizzas")
@RequiredArgsConstructor
public class PizzaController {
  private final PizzaService pizzaService;

  /**
   * Creates a new pizza.
   * <p>
   * <b>This API is for internal use by the pizzeria via the restaurant portal only.</b>
   *
   * @param model pizza model to create
   * @return created pizza
   */
  @Operation(summary = "Create a new pizza (internal use)", description = "Creates a pizza and returns the created model. This API is for internal use by the pizzeria via the restaurant portal only.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Pizza successfully created",
          content = @Content(schema = @Schema(implementation = PizzaModel.class))),
      @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
  })
  @PostMapping
  public ResponseEntity<PizzaModel> createPizza(
      @RequestBody @Valid PizzaModel model) {
    log.info("<<< POST /api/pizzas - createPizza called with model: {} >>>", model);
    return ResponseEntity.ok(pizzaService.createPizza(model));
  }

  /**
   * Returns the list of all pizzas.
   * <p>
   * <b>This API is for internal use by the pizzeria via the restaurant portal only.</b>
   *
   * @return list of pizzas
   */
  @Operation(summary = "Get all pizzas (internal use)", description = "Returns the list of all available pizzas. This API is for internal use by the pizzeria via the restaurant portal only.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of pizzas",
          content = @Content(schema = @Schema(implementation = PizzaModel.class)))
  })
  @GetMapping
  public ResponseEntity<List<PizzaModel>> getAllPizzas() {
    log.info("<<< GET /api/pizzas - getAllPizzas called >>>");
    return ResponseEntity.ok(pizzaService.getAllPizzas());
  }

  /**
   * Returns a list of pizzas by a list of IDs.
   * <p>
   * <b>This API is for internal use by the pizzeria via the restaurant portal only.</b>
   *
   * @param ids list of pizza IDs
   * @return list of found pizzas
   */
  @Operation(summary = "Get pizzas by IDs (internal use)", description = "Returns the list of pizzas matching the provided IDs. This API is for internal use by the pizzeria via the restaurant portal only.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of found pizzas",
          content = @Content(schema = @Schema(implementation = PizzaModel.class))),
      @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
  })
  @PostMapping("/by-ids")
  public ResponseEntity<List<PizzaModel>> getPizzasByIds(
      @RequestBody List<Long> ids) {
    log.info("<<< GET /api/pizzas/{} - getPizzasByIds called >>>", ids);
    return ResponseEntity.ok(pizzaService.getPizzasByIds(ids));
  }

  /**
   * Updates an existing pizza.
   * <p>
   * <b>This API is for internal use by the pizzeria via the restaurant portal only.</b>
   *
   * @param id pizza ID
   * @param model new pizza model
   * @return updated pizza
   */
  @Operation(summary = "Update a pizza (internal use)", description = "Updates an existing pizza by ID and the new model. This API is for internal use by the pizzeria via the restaurant portal only.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Pizza successfully updated",
          content = @Content(schema = @Schema(implementation = PizzaModel.class))),
      @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
      @ApiResponse(responseCode = "404", description = "Pizza not found", content = @Content)
  })
  @PutMapping("/{id}")
  public ResponseEntity<PizzaModel> updatePizza(
      @Parameter(description = "ID of the pizza to update", required = true)
      @PathVariable Long id,
      @RequestBody @Valid PizzaModel model) {
    log.info("<<< PUT /api/pizzas/{} - updatePizza called with model: {} >>>", id, model);
    return ResponseEntity.ok(pizzaService.updatePizza(model));
  }

  /**
   * Deletes a pizza by ID.
   * <p>
   * <b>This API is for internal use by the pizzeria via the restaurant portal only.</b>
   *
   * @param id pizza ID
   */
  @Operation(summary = "Delete a pizza (internal use)", description = "Deletes a pizza by its ID. This API is for internal use by the pizzeria via the restaurant portal only.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Pizza successfully deleted"),
      @ApiResponse(responseCode = "404", description = "Pizza not found", content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePizza(
      @Parameter(description = "ID of the pizza to delete", required = true)
      @PathVariable Long id) {
    log.info("<<< DELETE /api/pizzas/{} - deletePizza called >>>", id);
    return pizzaService.deletePizza(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
