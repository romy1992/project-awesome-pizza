package com.awesome.pizza.brick.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.awesome.pizza.brick.entity.Pizza;
import com.awesome.pizza.brick.mapper.PizzaMapper;
import com.awesome.pizza.brick.repository.PizzaRepository;
import com.awesome.pizza.commons.model.PizzaModel;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PizzaServiceTest {
    @Mock
    private PizzaRepository pizzaRepository;
    @Mock
    private PizzaMapper pizzaMapper;
    @Mock
    private IngredientService ingredientService;
    @InjectMocks
    private PizzaService pizzaService;

	private PizzaModel pizzaModel;

    @BeforeEach
    void setUp() {
        pizzaModel = new PizzaModel();
        pizzaModel.setId(1L);
        pizzaModel.setName("Margherita");
    }

    @Test
    void createPizza_shouldReturnCreatedPizza() {
        Pizza pizzaEntity = new Pizza();
        pizzaEntity.setId(1L);
        when(pizzaMapper.toEntity(any(PizzaModel.class))).thenReturn(pizzaEntity);
        when(pizzaRepository.saveAndFlush(any(Pizza.class))).thenReturn(pizzaEntity);
        when(pizzaMapper.toModel(any(Pizza.class))).thenReturn(pizzaModel);
        pizzaModel.setIngredients(Collections.emptyList());
        PizzaModel result = pizzaService.createPizza(pizzaModel);
        assertThat(result).isNotNull();
    }

    @Test
    void getAllPizzas_shouldReturnList() {
        when(pizzaRepository.findAll()).thenReturn(List.of(new Pizza()));
        when(pizzaMapper.toModel(any(Pizza.class))).thenReturn(pizzaModel);
        List<PizzaModel> result = pizzaService.getAllPizzas();
        assertThat(result).hasSize(1);
    }

    @Test
    void getAllPizzas_shouldReturnEmptyList() {
        when(pizzaRepository.findAll()).thenReturn(Collections.emptyList());
        List<PizzaModel> result = pizzaService.getAllPizzas();
        assertThat(result).isEmpty();
    }

    @Test
    void getPizzasByIds_shouldReturnList() {
        when(pizzaRepository.findAllById(any())).thenReturn(List.of(new Pizza()));
        when(pizzaMapper.toModel(any(Pizza.class))).thenReturn(pizzaModel);
        List<PizzaModel> result = pizzaService.getPizzasByIds(List.of(1L));
        assertThat(result).hasSize(1);
    }

    @Test
    void getPizzasByIds_shouldReturnEmptyList() {
        when(pizzaRepository.findAllById(any())).thenReturn(Collections.emptyList());
        List<PizzaModel> result = pizzaService.getPizzasByIds(List.of(99L));
        assertThat(result).isEmpty();
    }

    @Test
    void updatePizza_shouldReturnUpdatedPizza() {
        Pizza pizzaEntity = new Pizza();
        pizzaEntity.setId(1L);
        when(pizzaMapper.toEntity(any(PizzaModel.class))).thenReturn(pizzaEntity);
        when(pizzaRepository.saveAndFlush(any(Pizza.class))).thenReturn(pizzaEntity);
        when(pizzaMapper.toModel(any(Pizza.class))).thenReturn(pizzaModel);
        PizzaModel result = pizzaService.updatePizza(pizzaModel);
        assertThat(result).isNotNull();
    }

    @Test
    void deletePizza_shouldReturnTrueIfExists() {
        when(pizzaRepository.existsById(1L)).thenReturn(true);
        PizzaService spyService = Mockito.spy(pizzaService);
        Mockito.doNothing().when(pizzaRepository).deleteById(1L);
        boolean result = pizzaService.deletePizza(1L);
        assertThat(result).isTrue();
    }

    @Test
    void deletePizza_shouldReturnFalseIfNotExists() {
        when(pizzaRepository.existsById(99L)).thenReturn(false);
        boolean result = pizzaService.deletePizza(99L);
        assertThat(result).isFalse();
    }
}
