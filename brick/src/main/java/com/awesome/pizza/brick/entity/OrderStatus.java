package com.awesome.pizza.brick.entity;

/**
 * Enum representing the status of an order in Awesome Pizza.
 */
public enum OrderStatus {
    QUEUED,        // Order is in the queue
    IN_PROGRESS,   // Order is being prepared
    READY,         // Order is ready for pickup/delivery
    DELIVERED      // Order has been delivered
}

