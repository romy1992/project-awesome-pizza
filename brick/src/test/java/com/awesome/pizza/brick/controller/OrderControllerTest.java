package com.awesome.pizza.brick.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.awesome.pizza.brick.exception.OrderException;
import com.awesome.pizza.brick.service.OrderService;
import com.awesome.pizza.commons.model.OrderModel;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
@OverrideAutoConfiguration(enabled = true)
class OrderControllerTest {
  @Autowired private MockMvc mockMvc;
  @MockBean private OrderService orderService;
  private OrderModel orderModel;

  @BeforeEach
  void setUp() {
    orderModel = new OrderModel();
    orderModel.setCode("CODE123");
  }

  @Test
  void findAllOrdersByStatusAndPickupDate_returnsOk() throws Exception {
    when(orderService.findAllOrdersByStatusAndPickupDate(any(), any()))
        .thenReturn(List.of(orderModel));
    mockMvc
        .perform(
            get("/api/orders")
                .param("status", "QUEUED")
                .param("pickupDate", LocalDateTime.now().toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].code").value("CODE123"));
  }

  @Test
  void getOrderById_shouldReturnOk() throws Exception {
    when(orderService.getOrderById(1L)).thenReturn(orderModel);
    mockMvc
        .perform(get("/api/orders/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("CODE123"));
  }

  @Test
  void getOrderById_shouldReturnNotFound() throws Exception {
    when(orderService.getOrderById(99L)).thenThrow(new OrderException("Order not found"));
    mockMvc.perform(get("/api/orders/99")).andExpect(status().isNotFound());
  }

  @Test
  void getOrderByCode_shouldReturnOk() throws Exception {
    when(orderService.getOrderByCode("CODE123"))
        .thenReturn(OrderModel.builder().code("CODE123").build());
    mockMvc
        .perform(get("/api/orders/by-code").param("code", "CODE123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("CODE123"));
  }

  @Test
  void getOrderByCode_shouldReturnNotFound() throws Exception {
    when(orderService.getOrderByCode("NOTFOUND")).thenThrow(new OrderException("Order not found"));
    mockMvc
        .perform(get("/api/orders/by-code").param("code", "NOTFOUND"))
        .andExpect(status().isNotFound());
  }

  @Test
  void createOrder_shouldReturnOk() throws Exception {
    var response = new OrderModel();
    when(orderService.createOrder(any())).thenReturn(response);
    String validOrderJson =
        "{"
            + "\"user\": {\"name\": \"Mario\", \"pickupFrom\": \"2025-10-01T13:00:00\", \"pickupTo\": \"2025-10-01T14:00:00\"},"
            + "\"pizzas\": [{\"name\": \"Margherita\", \"price\": 5.0}]"
            + "}";
    mockMvc
        .perform(post("/api/orders").contentType("application/json").content(validOrderJson))
        .andExpect(status().isOk());
  }

  @Test
  void createOrder_shouldReturnBadRequest() throws Exception {
    // JSON non valido: manca 'user' e 'pizzas'
    String invalidOrderJson = "{}";
    mockMvc
        .perform(post("/api/orders").contentType("application/json").content(invalidOrderJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateOrder_shouldReturnOk() throws Exception {
    var response = new OrderModel();
    when(orderService.updateOrder(eq("CODE123"), any())).thenReturn(response);
    String validOrderJson =
        "{"
            + "\"user\": {\"name\": \"Mario\", \"pickupFrom\": \"2025-10-01T13:00:00\", \"pickupTo\": \"2025-10-01T14:00:00\"},"
            + "\"pizzas\": [{\"name\": \"Margherita\", \"price\": 5.0}]"
            + "}";
    mockMvc
        .perform(
            put("/api/orders/update")
                .param("code", "CODE123")
                .contentType("application/json")
                .content(validOrderJson))
        .andExpect(status().isOk());
  }

  @Test
  void updateOrder_shouldReturnNotFound() throws Exception {
    when(orderService.updateOrder(eq("NOTFOUND"), any()))
        .thenThrow(new OrderException("Order not found"));
    String validOrderJson =
        "{"
            + "\"user\": {\"name\": \"Mario\", \"pickupFrom\": \"2025-10-01T13:00:00\", \"pickupTo\": \"2025-10-01T14:00:00\"},"
            + "\"pizzas\": [{\"name\": \"Margherita\", \"price\": 5.0}]"
            + "}";
    mockMvc
        .perform(
            put("/api/orders/update")
                .param("code", "NOTFOUND")
                .contentType("application/json")
                .content(validOrderJson))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateOrderByCode_shouldReturnOk() throws Exception {
    when(orderService.updateOrderStatus(eq("CODE123"), eq("IN_PROGRESS"))).thenReturn(orderModel);
    mockMvc
        .perform(
            put("/api/orders/update-status")
                .param("code", "CODE123")
                .param("status", "IN_PROGRESS"))
        .andExpect(status().isOk());
  }

  @Test
  void updateOrderByCode_shouldReturnNotFound() throws Exception {
    when(orderService.updateOrderStatus(eq("NOTFOUND"), any()))
        .thenThrow(new OrderException("Order not found"));
    mockMvc
        .perform(
            put("/api/orders/update-status")
                .param("code", "NOTFOUND")
                .param("status", "IN_PROGRESS"))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteOrderByCode_shouldReturnNoContent() throws Exception {
    when(orderService.deleteByCode(eq("CODE123"), eq(true))).thenReturn(true);
    mockMvc
        .perform(delete("/api/orders").param("code", "CODE123").param("force", "true"))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteOrderByCode_shouldReturnNotFound() throws Exception {
    when(orderService.deleteByCode(eq("NOTFOUND"), eq(false))).thenReturn(false);
    mockMvc
        .perform(delete("/api/orders").param("code", "NOTFOUND").param("force", "false"))
        .andExpect(status().isNotFound());
  }
}
