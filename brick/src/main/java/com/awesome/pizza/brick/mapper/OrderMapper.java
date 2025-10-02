package com.awesome.pizza.brick.mapper;

import com.awesome.pizza.brick.entity.Order;
import com.awesome.pizza.brick.entity.OrderPizza;
import com.awesome.pizza.commons.model.OrderModel;
import com.awesome.pizza.commons.model.OrderPizzaModel;
import org.mapstruct.*;

/** Mapper for converting between Order entity and OrderModel DTO in Awesome Pizza. */
@Mapper(
    componentModel = "spring",
    uses = {PizzaMapper.class})
public interface OrderMapper {
  OrderModel toModel(Order order);

  @Mapping(target = "pizzaId", source = "pizza.id")
  @Mapping(target = "namePizza", source = "pizza.name")
  OrderPizzaModel toOrderPizzaModel(OrderPizza orderPizza);

  Order toEntity(OrderModel model);

	@Mapping(target = "pizza.id", source = "pizzaId")
	OrderPizza toOrderPizzaEntity(OrderPizzaModel orderPizza);
}
