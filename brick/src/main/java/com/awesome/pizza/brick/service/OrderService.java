package com.awesome.pizza.brick.service;

import com.awesome.pizza.brick.entity.Order;
import com.awesome.pizza.brick.mapper.OrderMapper;
import com.awesome.pizza.brick.repository.OrderRepository;
import com.awesome.pizza.commons.model.OrderModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing orders in Awesome Pizza.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;

	/**
	 * Creates a new order.
	 * @param model the order model to create
	 * @return the created order model
	 */
	public OrderModel createOrder(OrderModel model) {
		log.info("<<< OrderService.createOrder called with model: {} >>>", model);
		Order order = orderMapper.toEntity(model);
		Order saved = orderRepository.save(order);
		log.info("<<< Order created with id: {} >>>", saved.getId());
		return orderMapper.toModel(saved);
	}

	/**
	 * Returns a list of all orders.
	 * @return list of order models
	 */
	public List<OrderModel> getAllOrders() {
		log.info("<<< OrderService.getAllOrders called >>>");
		List<Order> orders = orderRepository.findAll();
		log.info("<<< Found {} orders >>>", orders.size());
		return orders.stream()
				.map(orderMapper::toModel)
				.collect(Collectors.toList());
	}

	/**
	 * Returns order details by ID.
	 * @param id the order identifier
	 * @return order model, if present
	 */
	public Optional<OrderModel> getOrderById(Long id) {
		log.info("<<< OrderService.getOrderById called with id: {} >>>", id);
		return orderRepository.findById(id).map(orderMapper::toModel);
	}

	/**
	 * Updates an existing order.
	 * @param id the order identifier
	 * @param model the new order model
	 * @return updated order model, if present
	 */
	public Optional<OrderModel> updateOrder(Long id, OrderModel model) {
		log.info("<<< OrderService.updateOrder called with id: {} and model: {} >>>", id, model);
		return orderRepository.findById(id).map(existing -> {
			Order updated = orderMapper.toEntity(model);
			updated.setId(id);
			Order saved = orderRepository.save(updated);
			log.info("<<< Order updated with id: {} >>>", saved.getId());
			return orderMapper.toModel(saved);
		});
	}

	/**
	 * Deletes an order by ID.
	 * @param id the order identifier
	 * @return true if the order was deleted, false otherwise
	 */
	public boolean deleteOrder(Long id) {
		log.info("<<< OrderService.deleteOrder called with id: {} >>>", id);
		if (orderRepository.existsById(id)) {
			orderRepository.deleteById(id);
			log.info("<<< Order with id: {} deleted >>>", id);
			return true;
		}
		log.warn("<<< Attempt to delete non-existing order with id: {} >>>", id);
		return false;
	}
}
