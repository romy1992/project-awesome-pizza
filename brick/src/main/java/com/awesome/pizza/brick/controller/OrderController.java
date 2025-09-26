package com.awesome.pizza.brick.controller;

import com.awesome.pizza.brick.service.OrderService;
import com.awesome.pizza.commons.model.OrderModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** REST controller for managing orders in Awesome Pizza. */
@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;

  /**
   * Creates a new order.
   * @param model the order model to create
   * @return the created order model
   */
  @PostMapping
  public ResponseEntity<OrderModel> createOrder(@RequestBody OrderModel model) {
    log.info("<<< POST /api/orders - createOrder called with model: {} >>>", model);
    return ResponseEntity.ok(orderService.createOrder(model));
  }

  /**
   * Returns a list of all orders.
   * @return list of order models
   */
  @GetMapping
  public ResponseEntity<List<OrderModel>> getAllOrders() {
    log.info("<<< GET /api/orders - getAllOrders called >>>");
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  /**
   * Returns order details by ID.
   * @param id the order identifier
   * @return order model, if present
   */
  @GetMapping("/{id}")
  public ResponseEntity<OrderModel> getOrderById(@PathVariable Long id) {
    log.info("<<< GET /api/orders/{} - getOrderById called >>>", id);
    return orderService
        .getOrderById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Updates an existing order.
   * @param id the order identifier
   * @param model the new order model
   * @return updated order model, if present
   */
  @PutMapping("/{id}")
  public ResponseEntity<OrderModel> updateOrder(
      @PathVariable Long id, @RequestBody OrderModel model) {
    log.info("<<< PUT /api/orders/{} - updateOrder called with model: {} >>>", id, model);
    return orderService
        .updateOrder(id, model)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Deletes an order by ID.
   * @param id the order identifier
   * @return no content if deleted, not found otherwise
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
    log.info("<<< DELETE /api/orders/{} - deleteOrder called >>>", id);
    return orderService.deleteOrder(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
