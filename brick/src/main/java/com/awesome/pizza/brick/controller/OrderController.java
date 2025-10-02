package com.awesome.pizza.brick.controller;

import com.awesome.pizza.brick.model.RequestUserOrderModel;
import com.awesome.pizza.brick.service.OrderService;
import com.awesome.pizza.commons.model.OrderModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
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
   * Creates a new order and returns the summary of the created order.
   *
   * @param model model with user data and requested pizzas
   * @return summary of the created order
   */
  @Operation(
      summary = "Create a new order",
      description = "Creates an order and returns the summary.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Order successfully created",
            content = @Content(schema = @Schema(implementation = OrderModel.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
      })
  @PostMapping
  public ResponseEntity<OrderModel> createOrder(@RequestBody @Valid RequestUserOrderModel model) {
    log.info("<<< POST /api/orders - createOrder called with model: {} >>>", model);
    return ResponseEntity.ok(orderService.createOrder(model));
  }

  /**
   * Returns all orders, optionally filtered by status and pickup date.
   *
   * @param statuses list of order statuses (optional)
   * @param pickupDate pickup date (optional)
   * @return list of orders
   */
  @Operation(
      summary = "Get all orders",
      description = "Retrieves all orders, optionally filtered by status and pickup date.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of orders",
            content = @Content(schema = @Schema(implementation = OrderModel.class)))
      })
  @GetMapping
  public ResponseEntity<List<OrderModel>> findAllOrdersByStatusAndPickupDate(
      @Parameter(description = "List of order statuses to filter by", required = false)
          @RequestParam(value = "statuses", required = false)
          List<String> statuses,
      @Parameter(description = "Pickup date to filter by", required = false)
          @RequestParam(value = "pickupDate", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime pickupDate) {
    log.info(
        "<<< GET /api/orders - findAllOrdersByStatusAndPickupDate called with statuses: {} and pickupDate: {} >>>",
        statuses,
        pickupDate);
    return ResponseEntity.ok(orderService.findAllOrdersByStatusAndPickupDate(statuses, pickupDate));
  }

  /**
   * Returns the details of an order by its ID.
   *
   * @param id order identifier
   * @return order details
   */
  @Operation(
      summary = "Get order by ID",
      description = "Returns the details of an order by its ID.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Order found",
            content = @Content(schema = @Schema(implementation = OrderModel.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
      })
  @GetMapping("/{id}")
  public ResponseEntity<OrderModel> getOrderById(
      @Parameter(description = "Order ID", required = true) @PathVariable Long id) {
    log.info("<<< GET /api/orders/{} - getOrderById called >>>", id);
    return ResponseEntity.ok(orderService.getOrderById(id));
  }

  /**
   * Returns the details of an order by its code.
   *
   * @param code order code
   * @return order details
   */
  @Operation(
      summary = "Get order by code",
      description = "Returns the details of an order by its code.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Order found",
            content = @Content(schema = @Schema(implementation = OrderModel.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
      })
  @GetMapping("/by-code")
  public ResponseEntity<OrderModel> getOrderByCode(
      @Parameter(description = "Order code", required = true) @RequestParam("code") String code) {
    log.info("<<< GET /api/orders/by-code - getOrderByCode called with code: {} >>>", code);
    return ResponseEntity.ok(orderService.getOrderByCode(code));
  }

  /**
   * Updates an existing order. This endpoint is intended for the user to modify their order, but
   * only if the order is still in QUEUED status (i.e., before it is taken in charge by the pizza
   * chef). Once the order is in progress, it can no longer be modified by the user.
   */
  @Operation(
      summary = "Update an order (user only)",
      description =
          "Allows the user to update their order, but only if the order is still in QUEUED status (before it is taken in charge by the pizza chef).")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Order successfully updated",
            content = @Content(schema = @Schema(implementation = OrderModel.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
      })
  @PutMapping("update")
  public ResponseEntity<OrderModel> updateOrder(
      @Parameter(description = "Order code to update", required = true) @RequestParam("code")
          String code,
      @RequestBody @Valid RequestUserOrderModel model) {
    log.info(
        "<<< PUT /api/orders - updateOrder called with code {} and model: {} >>>", code, model);
    return ResponseEntity.ok(orderService.updateOrder(code, model));
  }

  /**
   * Updates the status of an existing order. This endpoint is intended for the pizza chef only.
   * Only the pizza chef can change the status of an order (e.g., from QUEUED to IN_PROGRESS or
   * COMPLETED).
   */
  @Operation(
      summary = "Update order status (pizza chef only)",
      description =
          "Allows only the pizza chef to update the status of an order (e.g., from QUEUED to IN_PROGRESS or COMPLETED). Users cannot access this endpoint.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Order status successfully updated",
            content = @Content(schema = @Schema(implementation = OrderModel.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
      })
  @PutMapping("/update-status")
  public ResponseEntity<OrderModel> updateOrderByCode(
      @Parameter(description = "Order code to update", required = true) @RequestParam("code")
          String code,
      @Parameter(description = "New order status", required = true) @RequestParam("status")
          String status) {
    log.info(
        "<<< PUT /api/orders - updateOrderByCode called with code: {} and model: {} >>>",
        code,
        status);
    return ResponseEntity.ok(orderService.updateOrderStatus(code, status));
  }

  /**
   * Deletes an order by its code. This endpoint can be used by both the user and the pizza chef.
   *
   * <ul>
   *   <li>The user can delete the order only if it is in QUEUED status (force = false).
   *   <li>The pizza chef can delete the order in any status by setting force = true.
   * </ul>
   */
  @Operation(
      summary = "Delete order by code (user or pizza chef)",
      description =
          "Deletes an order by its code. The user can delete the order only if it is in QUEUED status (force = false). The pizza chef can delete the order in any status by setting force = true.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Order successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
      })
  @DeleteMapping
  public ResponseEntity<Void> deleteOrderByCode(
      @Parameter(description = "Order code to delete", required = true) @RequestParam("code")
          String code,
      @Parameter(
              description =
                  "Force delete flag. Set to true to allow the pizza chef to delete any order, false to allow the user to delete only QUEUED orders.",
              required = true)
          @RequestParam("force")
          boolean forceDelete) {
    log.info("<<< DELETE /api/orders - deleteOrderByCode called with code: {} >>>", code);
    boolean deleted = orderService.deleteByCode(code, forceDelete);
    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
