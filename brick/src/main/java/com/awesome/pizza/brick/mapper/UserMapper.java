package com.awesome.pizza.brick.mapper;

import com.awesome.pizza.brick.entity.User;
import com.awesome.pizza.commons.model.UserModel;
import org.mapstruct.*;

/**
 * Mapper for converting between User entity and UserModel DTO in Awesome Pizza.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserModel toModel(User user);
    User toEntity(UserModel model);
}

