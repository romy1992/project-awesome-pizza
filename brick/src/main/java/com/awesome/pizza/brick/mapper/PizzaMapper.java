package com.awesome.pizza.brick.mapper;

import com.awesome.pizza.brick.entity.Pizza;
import com.awesome.pizza.commons.model.PizzaModel;
import org.mapstruct.Mapper;

/** Mapper for converting between Pizza entity and PizzaModel DTO in Awesome Pizza. */
@Mapper(componentModel = "spring", uses = IngredientMapper.class)
public interface PizzaMapper {
  PizzaModel toModel(Pizza pizza);

  Pizza toEntity(PizzaModel model);
}
