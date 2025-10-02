package com.awesome.pizza.brick.exception;

/** Custom exception for order-related errors in Awesome Pizza. */
public class OrderException extends RuntimeException {

  /**
   * Constructs a new OrderException with the specified detail message.
   *
   * @param message the detail message
   */
  public OrderException(String message) {
    super(message);
  }
}
