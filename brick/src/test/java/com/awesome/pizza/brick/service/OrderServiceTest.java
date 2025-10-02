package com.awesome.pizza.brick.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.awesome.pizza.brick.entity.Order;
import com.awesome.pizza.brick.entity.Pizza;
import com.awesome.pizza.brick.entity.User;
import com.awesome.pizza.brick.exception.OrderException;
import com.awesome.pizza.brick.mapper.OrderMapper;
import com.awesome.pizza.brick.mapper.UserMapper;
import com.awesome.pizza.brick.model.RequestUserOrderModel;
import com.awesome.pizza.brick.repository.OrderRepository;
import com.awesome.pizza.brick.repository.PizzaRepository;
import com.awesome.pizza.commons.model.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
  @Mock private OrderRepository orderRepository;
  @Mock private PizzaRepository pizzaRepository;
  @Mock private OrderMapper orderMapper;
  @Mock private UserMapper userMapper;
  @InjectMocks private OrderService orderService;

  RequestUserOrderModel request;

  @BeforeEach
  void setUp() {
    request = getRequestUserOrderModel();
  }

  @Test
  void findAllOrdersByStatusAndPickupDate_withNullParams_returnsAll() {
    Order order =
        Order.builder()
            .id(2L)
            .status(OrderStatus.IN_PROGRESS)
            .user(
                User.builder()
                    .pickupFrom(LocalDateTime.now())
                    .pickupTo(LocalDateTime.now().plusHours(1))
                    .build())
            .build();
    when(orderRepository.findAllByStatusAndPickupDate(null, null)).thenReturn(List.of(order));
    OrderModel orderModel = new OrderModel();
    when(orderMapper.toModel(order)).thenReturn(orderModel);
    List<OrderModel> result = orderService.findAllOrdersByStatusAndPickupDate(null, null);
    assertThat(result).hasSize(1);
  }

  @Test
  void getOrderByCode_returnsOrder() {
    Order order = Order.builder().id(4L).status(OrderStatus.QUEUED).build();
    when(orderRepository.findByCode("CODE123")).thenReturn(order);
    when(orderMapper.toModel(order)).thenReturn(new OrderModel());
    OrderModel result = orderService.getOrderByCode("CODE123");
    assertThat(result).isNotNull();
  }

  @Test
  void getOrderById_returnsOptional() {
    Order order = Order.builder().id(5L).status(OrderStatus.QUEUED).build();
    when(orderRepository.findById(5L)).thenReturn(Optional.of(order));
    OrderModel orderModel = new OrderModel();
    when(orderMapper.toModel(order)).thenReturn(orderModel);
    OrderModel result = orderService.getOrderById(5L);
    assertThat(result).isNotNull();
  }

  @Test
  void getOrderById_shouldReturnOrderModelIfExists() {
    Order order = Order.builder().id(1L).build();
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    OrderModel orderModel = new OrderModel();
    when(orderMapper.toModel(order)).thenReturn(orderModel);
    OrderModel result = orderService.getOrderById(1L);
    assertThat(result).isNotNull();
  }

  @Test
  void getOrderById_shouldThrowIfNotExists() {
    when(orderRepository.findById(99L)).thenReturn(Optional.empty());
    Assertions.assertThrows(OrderException.class, () -> orderService.getOrderById(99L));
  }

  @Test
  void getOrderByCode_shouldReturnOrderModel() {
    Order order = Order.builder().id(1L).code("CODE123").build();
    when(orderRepository.findByCode("CODE123")).thenReturn(order);
    when(orderMapper.toModel(order)).thenReturn(new OrderModel());
    OrderModel result = orderService.getOrderByCode("CODE123");
    assertThat(result).isNotNull();
  }

  @Test
  void getOrderByCode_shouldThrowIfNotFound() {
    when(orderRepository.findByCode("NOTFOUND")).thenReturn(null);
    Assertions.assertThrows(RuntimeException.class, () -> orderService.getOrderByCode("NOTFOUND"));
  }

  @Test
  void createOrder_shouldReturnResponseUserOrderModel() {
    Pizza pizza = new Pizza();
    pizza.setId(1L);
    pizza.setPrice(java.math.BigDecimal.TEN);
    request.setPizzas(request.getPizzas());

    when(orderRepository.saveAndFlush(any())).thenReturn(new Order());
    when(pizzaRepository.findAllById(any())).thenReturn(List.of(pizza));
    when(orderMapper.toModel(any())).thenReturn(new OrderModel());
    when(userMapper.toEntity(any())).thenReturn(new User());
    Object result = orderService.createOrder(request);
    assertThat(result).isNotNull();
  }

  @Test
  void createOrder_shouldThrowIfNoUserOrPizzas() {
    Assertions.assertThrows(OrderException.class, () -> orderService.createOrder(request));
  }

  @Test
  void updateOrder_shouldReturnResponseUserOrderModel() {
    String code = "ORD-1";
    Pizza pizza = Pizza.builder().id(1L).price(java.math.BigDecimal.TEN).build();
    Order order =
        Order.builder()
            .id(1L)
            .code(code)
            .status(OrderStatus.QUEUED)
            .orderedPizzas(new java.util.ArrayList<>())
            .build();
    when(orderRepository.findByCode(code)).thenReturn(order);
    when(orderRepository.saveAndFlush(any())).thenReturn(order);
    when(pizzaRepository.findAllById(any())).thenReturn(List.of(pizza));
    when(orderMapper.toModel(any())).thenReturn(new OrderModel());
    when(userMapper.toEntity(any())).thenReturn(new User());
    OrderModel result = orderService.updateOrder(code, request);
    assertThat(result).isNotNull();
  }

  @Test
  void updateOrder_shouldThrowIfOrderNotFound() {
    String code = "ORD-404";
    when(orderRepository.findByCode(code)).thenReturn(null);
    Assertions.assertThrows(OrderException.class, () -> orderService.updateOrder(code, request));
  }

  @Test
  void updateOrderStatus_shouldUpdateStatusSuccessfully() {
    String code = "ORD-2";
    Order order =
        Order.builder()
            .id(2L)
            .code(code)
            .status(OrderStatus.QUEUED)
            .orderedPizzas(new java.util.ArrayList<>())
            .build();
    when(orderRepository.findByCode(code)).thenReturn(order);
    when(orderRepository.saveAndFlush(any(Order.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    OrderModel orderModel = new OrderModel();
    when(orderMapper.toModel(order)).thenReturn(orderModel);

    OrderModel result = orderService.updateOrderStatus(code, OrderStatus.IN_PROGRESS.name());
    assertThat(result).isNotNull();
    assertThat(order.getStatus()).isEqualTo(OrderStatus.IN_PROGRESS);
  }

  @Test
  void updateOrderStatus_shouldThrowIfOrderNotFound() {
    String code = "ORD-404";
    when(orderRepository.findByCode(code)).thenReturn(null);
    Assertions.assertThrows(
        OrderException.class,
        () -> orderService.updateOrderStatus(code, OrderStatus.IN_PROGRESS.name()));
  }

  // Helper method to create a sample RequestUserOrderModel
  private RequestUserOrderModel getRequestUserOrderModel() {
    return RequestUserOrderModel.builder()
        .user(createUserModel())
        .pizzas(List.of(createOrderPizzaModel()))
        .build();
  }

  private UserModel createUserModel() {
    return UserModel.builder()
        .id(1L)
        .name("Mario")
        .pickupFrom(LocalDateTime.now().plusHours(1))
        .pickupTo(LocalDateTime.now().plusHours(2))
        .userComment("No onions, please.")
        .build();
  }

  private OrderPizzaModel createOrderPizzaModel() {
    return OrderPizzaModel.builder()
        .idOrderPizza(1L)
        .pizzaId(1L)
        .descriptionUser("Test")
        .namePizza("Margherita")
        .build();
  }
}
