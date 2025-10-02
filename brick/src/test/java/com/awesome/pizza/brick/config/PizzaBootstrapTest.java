package com.awesome.pizza.brick.config;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.awesome.pizza.brick.service.PizzaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PizzaBootstrapTest {
  @Mock private PizzaService pizzaService;

  @InjectMocks private PizzaBootstrap pizzaBootstrap;

  @Test
  void run_shouldCallPizzaService() {
    pizzaBootstrap.run();
    verify(pizzaService, times(10)).createPizza(ArgumentMatchers.any());
  }
}
