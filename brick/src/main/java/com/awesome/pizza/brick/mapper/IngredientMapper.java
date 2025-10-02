package com.awesome.pizza.brick.mapper;

import com.awesome.pizza.brick.entity.Ingredient;
import com.awesome.pizza.commons.model.IngredientModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    IngredientModel toModel(Ingredient ingredient);
    Ingredient toEntity(IngredientModel model);
}
