package com.awesome.pizza.brick.service;

import com.awesome.pizza.brick.entity.Order;
import com.awesome.pizza.brick.entity.OrderPizza;
import com.awesome.pizza.brick.entity.Pizza;
import com.awesome.pizza.brick.exception.OrderException;
import com.awesome.pizza.brick.mapper.OrderMapper;
import com.awesome.pizza.brick.mapper.UserMapper;
import com.awesome.pizza.brick.model.RequestUserOrderModel;
import com.awesome.pizza.brick.repository.OrderRepository;
import com.awesome.pizza.brick.repository.PizzaRepository;
import com.awesome.pizza.commons.model.OrderModel;
import com.awesome.pizza.commons.model.OrderPizzaModel;
import com.awesome.pizza.commons.model.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Service for managing orders in Awesome Pizza. */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final PizzaRepository pizzaRepository;
  private final UserMapper userMapper;

  /**
   * Creates a new order and returns the summary model.
   *
   * @param model order data to create
   * @return order summary model
   */
  public OrderModel createOrder(RequestUserOrderModel model) {
    log.info("<<< OrderService.createOrder called with model: {} >>>", model);
    // Validate the order
    checkOrderValid(model);
    // Check date validity
    checkValidDate(model);

    // Retrieve Pizza entities from the database for each requested pizza
    List<OrderPizza> pizzas = getPizzaModels(model);
    Order order =
        Order.builder()
            .code(generateOrderNumber(model.getUser().getName()))
            .createdAt(LocalDateTime.now())
            .status(OrderStatus.QUEUED)
            .orderedPizzas(pizzas)
            .user(userMapper.toEntity(model.getUser()))
            .totalPrice(calculateTotalPrice(pizzas))
            .build();
    return orderMapper.toModel(orderRepository.saveAndFlush(order));
  }

  /**
   * Validates the pickup date range.
   *
   * @param model order model
   * @throws OrderException if the pickup date range is invalid
   */
  private void checkValidDate(RequestUserOrderModel model) {
    if (Objects.nonNull(model.getUser().getPickupFrom())
        && Objects.nonNull(model.getUser().getPickupTo())
        && (model.getUser().getPickupFrom().isAfter(model.getUser().getPickupTo())
            || model.getUser().getPickupFrom().toLocalDate().isBefore(LocalDate.now())
            || model.getUser().getPickupTo().toLocalDate().isBefore(LocalDate.now())))
      throw new OrderException(
          "Invalid pickup date range: 'pickupFrom' is after 'pickupTo' or in the past.");
  }

  /**
   * Calculates the total price of the order.
   *
   * @param pizzas list of ordered pizzas
   * @return total price
   * @throws OrderException if there is an error calculating the price
   */
  private BigDecimal calculateTotalPrice(List<OrderPizza> pizzas) {
    return pizzas.stream()
        .map(OrderPizza::getPizza)
        .filter(Objects::nonNull)
        .map(Pizza::getPrice)
        .filter(Objects::nonNull)
        .reduce(BigDecimal::add)
        .orElseThrow(() -> new OrderException("Error calculating total price."));
  }

  /**
   * Retrieves Pizza entities from the database for each requested pizza.
   *
   * @param model order model
   * @return list of OrderPizza entities
   * @throws OrderException if no valid pizzas are found
   */
  private List<OrderPizza> getPizzaModels(RequestUserOrderModel model) {
    // set description for each pizza in the order
    List<OrderPizza> orderPizzas = new ArrayList<>();
    model
        .getPizzas()
        .forEach(
            userOrderPizza ->
                // Request only the pizzas in the order in a single query to the database
                pizzaRepository
                    .findAllById(
                        model.getPizzas().stream().map(OrderPizzaModel::getPizzaId).toList())
                    .stream()
                    .filter(p -> p.getId().equals(userOrderPizza.getPizzaId()))
                    .findFirst()
                    .ifPresent(
                        p ->
                            orderPizzas.add(
                                OrderPizza.builder()
                                    .pizza(p)
                                    .descriptionUser(userOrderPizza.getDescriptionUser())
                                    .build())));
    // Check if at least one pizza was found
    if (orderPizzas.isEmpty()) throw new OrderException("No valid pizzas found for the order.");
    return orderPizzas;
  }

  /**
   * Generates a simple order number.
   *
   * @param nameUser user's name
   * @return generated order number
   */
  private String generateOrderNumber(String nameUser) {
    return "ORD-" + System.currentTimeMillis() + "-" + nameUser.toUpperCase().trim();
  }

  /**
   * Validates the order model.
   *
   * @param model the order model to validate
   * @throws OrderException if the order is invalid
   */
  private void checkOrderValid(RequestUserOrderModel model) {
    if (Objects.isNull(model.getUser())
        || Objects.isNull(model.getPizzas())
        || model.getPizzas().isEmpty())
      throw new OrderException("Order must have a user and at least one pizza.");
  }

  /**
   * Returns all orders filtered by status and pickup date.
   *
   * @param statuses list of order statuses
   * @param pickupDate pickup date
   * @return list of orders
   */
  public List<OrderModel> findAllOrdersByStatusAndPickupDate(
      List<String> statuses, LocalDateTime pickupDate) {
    log.info(
        "<<< OrderService.findAllOrdersByStatusAndPickupDate called with status: {} and pickupDate: {} >>>",
        statuses,
        pickupDate);
    LocalDate date = Objects.nonNull(pickupDate) ? pickupDate.toLocalDate() : null;
    List<Order> orders = orderRepository.findAllByStatusAndPickupDate(statuses, date);
    log.info(
        "<<< OrderService.findAllOrdersByStatusAndPickupDate Found {} orders >>>", orders.size());
    return sortOrders(orders);
  }

  /**
   * Sorts orders by pickupFrom, pickupTo, and createdAt.
   *
   * @param orders list of orders
   * @return sorted list of order models
   */
  private List<OrderModel> sortOrders(List<Order> orders) {
    return orders.stream()
        .map(orderMapper::toModel)
        .sorted(
            Comparator.comparing(
                    (OrderModel o) -> o.getUser() != null ? o.getUser().getPickupFrom() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(
                    o -> o.getUser() != null ? o.getUser().getPickupTo() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(
                    OrderModel::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())))
        .toList();
  }

  /**
   * Returns the details of an order by its ID.
   *
   * @param id order identifier
   * @return order model
   */
  public OrderModel getOrderById(Long id) {
    log.info("<<< OrderService.getOrderById called with id: {} >>>", id);
    return orderMapper.toModel(
        orderRepository
            .findById(id)
            .orElseThrow(() -> new OrderException("Order with id " + id + " not found")));
  }

  /**
   * Returns the details of an order by its code.
   *
   * @param code order code
   * @return order model
   * @throws OrderException if the order is not found
   */
  public OrderModel getOrderByCode(String code) {
    log.info("<<< OrderService.getOrderByCode called with code: {} >>>", code);
    Order order = orderRepository.findByCode(code);
    if (Objects.isNull(order)) throw new OrderException("Order with code " + code + " not found");
    return orderMapper.toModel(order);
  }

  /**
   * Updates an order by its code.
   *
   * @param code order code
   * @param model new order model
   * @return updated order model
   * @throws OrderException if trying to update an order not in QUEUED status
   */
  public OrderModel updateOrder(String code, RequestUserOrderModel model) {
    log.info("<<< OrderService.updateOrder called with code: {} and model: {} >>>", code, model);
    // Validate the order
    checkOrderValid(model);
    // Check date validity
    checkValidDate(model);

    Order order = orderRepository.findByCode(code);
    if (Objects.nonNull(order)) {
      // Only QUEUED orders can be updated
      if (checkStatus(order.getStatus()))
        throw new OrderException("Only QUEUED orders can be updated.");

      // Retrieve Pizza entities from the database for each requested pizza
      List<OrderPizza> pizzas = getPizzaModels(model);

      // Update the existing list to avoid problems with orphanRemoval
      order.getOrderedPizzas().clear();
      order.getOrderedPizzas().addAll(pizzas);
      order.setUser(userMapper.toEntity(model.getUser()));
      order.setTotalPrice(calculateTotalPrice(pizzas));
      order = orderRepository.saveAndFlush(order);

      log.info("<<< Order updated with id: {} >>>", order.getId());
      return orderMapper.toModel(order);
    }
    throw new OrderException("Order with code " + code + " not found");
  }

  /**
   * Updates the status of an order by its code.
   *
   * @param code order code
   * @param status new status
   * @return updated order model
   */
  public OrderModel updateOrderStatus(String code, String status) {
    log.info(
        "<<< OrderService.updateOrderStatus called with code: {} and status: {} >>>", code, status);
    // If the new status is IN_PROGRESS, check that no other order is already IN_PROGRESS
    if (OrderStatus.IN_PROGRESS.name().equalsIgnoreCase(status)) {
      boolean alreadyInProgress =
          orderRepository.findAll().stream()
              .anyMatch(o -> o.getStatus() == OrderStatus.IN_PROGRESS && !o.getCode().equals(code));
      if (alreadyInProgress) {
        throw new OrderException(
            "There is already an order IN_PROGRESS. Only one order can be in progress at a time.");
      }
    }
    Order order = orderRepository.findByCode(code);
    if (Objects.isNull(order)) throw new OrderException("Order with code " + code + " not found");

    order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
    Order saved = orderRepository.saveAndFlush(order);
    log.info("<<< Order status updated with id: {} to status: {} >>>", saved.getId(), status);
    return orderMapper.toModel(saved);
  }

  /**
   * Deletes an order by its code.
   *
   * @param code order code
   * @param forceDelete force delete flag
   * @return true if deleted, false otherwise
   * @throws OrderException if trying to delete an order not in QUEUED status without forceDelete
   */
  public boolean deleteByCode(String code, boolean forceDelete) {
    log.info("<<< OrderService.deleteByCode called with code: {} >>>", code);
    Order order = orderRepository.findByCode(code);
    if (Objects.nonNull(order)) {

      // Only QUEUED orders can be deleted
      if (checkStatus(order.getStatus()) && !forceDelete)
        throw new OrderException("Only QUEUED orders can be deleted.");

      orderRepository.delete(order);
      log.info("<<< Order with code: {} deleted >>>", code);
      return true;
    }
    log.warn("<<< Attempt to delete non-existing order with code: {} >>>", code);
    return false;
  }

  /**
   * Checks if only QUEUED orders can be updated or deleted.
   *
   * @param status order status
   * @return true if not QUEUED, false otherwise
   */
  private boolean checkStatus(OrderStatus status) {
    return !status.equals(OrderStatus.QUEUED);
  }
}
