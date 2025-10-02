package com.awesome.pizza.brick.exception;

import java.time.LocalDateTime;

/**
 * Represents a standardized error response structure for API responses in Awesome Pizza.
 *
 * <p>Includes a timestamp, error message, and HTTP status code.
 */
public record ErrorResponse(LocalDateTime timestamp, String message, int status) {}
