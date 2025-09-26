package com.awesome.pizza.brick.exception;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ErrorResponse {
  private final LocalDateTime timestamp;
  private final String message;
  private final int status;

  public ErrorResponse(LocalDateTime timestamp, String message, int status) {
    this.timestamp = timestamp;
    this.message = message;
    this.status = status;
  }
}
