package com.awesome.pizza.brick.mapper;

import com.awesome.pizza.brick.entity.Order;
import com.awesome.pizza.commons.model.OrderModel;
import org.mapstruct.*;

/** Mapper for converting between Order entity and OrderModel DTO in Awesome Pizza. */
@Mapper(
    componentModel = "spring",
    uses = {PizzaMapper.class})
public interface OrderMapper {
  OrderModel toModel(Order order);

  Order toEntity(OrderModel model);
}
