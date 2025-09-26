package com.awesome.pizza.brick.config;

import com.awesome.pizza.brick.service.PizzaService;
import com.awesome.pizza.commons.model.PizzaModel;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PizzaBootstrap implements CommandLineRunner {
    private final PizzaService pizzaService;

    private static final List<PizzaModel> DEFAULT_PIZZAS = Arrays.asList(
        createPizza("Margherita", Arrays.asList("Pomodoro", "Mozzarella", "Basilico"), 7.50),
        createPizza("Diavola", Arrays.asList("Pomodoro", "Mozzarella", "Salame piccante"), 8.00),
        createPizza("Prosciutto e Funghi", Arrays.asList("Pomodoro", "Mozzarella", "Prosciutto", "Funghi"), 8.50),
        createPizza("Quattro Stagioni", Arrays.asList("Pomodoro", "Mozzarella", "Carciofi", "Funghi", "Prosciutto", "Olive"), 9.00),
        createPizza("Capricciosa", Arrays.asList("Pomodoro", "Mozzarella", "Carciofi", "Funghi", "Prosciutto", "Uovo"), 9.00),
        createPizza("Quattro Formaggi", Arrays.asList("Mozzarella", "Gorgonzola", "Fontina", "Parmigiano"), 9.50),
        createPizza("Vegetariana", Arrays.asList("Pomodoro", "Mozzarella", "Verdure grigliate"), 8.50),
        createPizza("Tonno e Cipolla", Arrays.asList("Pomodoro", "Mozzarella", "Tonno", "Cipolla"), 8.50),
        createPizza("Bufalina", Arrays.asList("Pomodoro", "Mozzarella di bufala", "Basilico"), 9.00),
        createPizza("Salsiccia e Friarielli", Arrays.asList("Mozzarella", "Salsiccia", "Friarielli"), 9.50)
    );

    private static PizzaModel createPizza(String name, List<String> ingredients, double price) {
        PizzaModel pizza = new PizzaModel();
        pizza.setName(name);
        pizza.setIngredients(ingredients);
        pizza.setPrice(price);
        return pizza;
    }

    @Override
    public void run(String... args) {
        List<PizzaModel> existingPizzas = pizzaService.getAllPizzas();
        if (existingPizzas.size() < 10) {
            int toCreate = 10 - existingPizzas.size();
            DEFAULT_PIZZAS.stream()
                .limit(toCreate)
                .forEach(pizzaService::createPizza);
        }
    }
}
