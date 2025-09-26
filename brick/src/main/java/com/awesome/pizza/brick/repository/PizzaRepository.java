package com.awesome.pizza.brick.repository;

import com.awesome.pizza.brick.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing Pizza entities in Awesome Pizza.
 */
@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    // Custom query methods can be added here
}
