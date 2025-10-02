package com.awesome.pizza.brick.controller;

import com.awesome.pizza.brick.service.PizzaService;
import com.awesome.pizza.commons.model.IngredientModel;
import com.awesome.pizza.commons.model.PizzaModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PizzaController.class)
class PizzaControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private PizzaService pizzaService;

  private PizzaModel pizzaModel;

  @BeforeEach
  void setUp() {
    pizzaModel = new PizzaModel();
    pizzaModel.setName("Margherita");
    pizzaModel.setDescription("Pomodoro, mozzarella, basilico");
    pizzaModel.setPrice(BigDecimal.valueOf(7.5));
    IngredientModel ingredient = new IngredientModel();
    ingredient.setName("Pomodoro");
    pizzaModel.setIngredients(List.of(ingredient));
  }

  @Test
  void createPizza_shouldReturnOk() throws Exception {
    when(pizzaService.createPizza(any(PizzaModel.class))).thenReturn(pizzaModel);
    String pizzaJson = "{" +
        "\"name\":\"Margherita\"," +
        "\"description\":\"Pomodoro, mozzarella, basilico\"," +
        "\"price\":7.5," +
        "\"ingredients\":[{\"name\":\"Pomodoro\"}]" +
        "}";
    mockMvc
        .perform(
            post("/api/pizzas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pizzaJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Margherita"));
  }

  @Test
  void getAllPizzas_shouldReturnOk() throws Exception {
    when(pizzaService.getAllPizzas()).thenReturn(List.of(pizzaModel));
    mockMvc
        .perform(get("/api/pizzas"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Margherita"));
  }

  @Test
  void getAllPizzas_shouldReturnEmptyList() throws Exception {
    when(pizzaService.getAllPizzas()).thenReturn(Collections.emptyList());
    mockMvc.perform(get("/api/pizzas")).andExpect(status().isOk()).andExpect(content().json("[]"));
  }

  @Test
  void getPizzasByIds_shouldReturnOk() throws Exception {
    when(pizzaService.getPizzasByIds(any())).thenReturn(List.of(pizzaModel));
    mockMvc
        .perform(post("/api/pizzas/by-ids").contentType(MediaType.APPLICATION_JSON).content("[1]"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Margherita"));
  }

  @Test
  void getPizzasByIds_shouldReturnEmptyList() throws Exception {
    when(pizzaService.getPizzasByIds(any())).thenReturn(Collections.emptyList());
    mockMvc
        .perform(post("/api/pizzas/by-ids").contentType(MediaType.APPLICATION_JSON).content("[99]"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  @Test
  void updatePizza_shouldReturnOk() throws Exception {
    when(pizzaService.updatePizza(any(PizzaModel.class))).thenReturn(pizzaModel);
    String pizzaJson = "{" +
        "\"id\":1," +
        "\"name\":\"Margherita\"," +
        "\"description\":\"Pomodoro, mozzarella, basilico\"," +
        "\"price\":7.5," +
        "\"ingredients\":[{\"name\":\"Pomodoro\"}]" +
        "}";
    mockMvc
        .perform(
            put("/api/pizzas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pizzaJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Margherita"));
  }

  @Test
  void deletePizza_shouldReturnNoContent() throws Exception {
    when(pizzaService.deletePizza(1L)).thenReturn(true);
    mockMvc.perform(delete("/api/pizzas/1")).andExpect(status().isNoContent());
  }

  @Test
  void deletePizza_shouldReturnNotFound() throws Exception {
    when(pizzaService.deletePizza(99L)).thenReturn(false);
    mockMvc.perform(delete("/api/pizzas/99")).andExpect(status().isNotFound());
  }
}
