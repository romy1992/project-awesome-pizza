package com.awesome.pizza.brick.exception;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
    ErrorResponse error =
        new ErrorResponse(
            LocalDateTime.now(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    log.error("Unhandled exception: ", ex);
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(OrderException.class)
  public ResponseEntity<ErrorResponse> handleOrderException(OrderException ex, WebRequest request) {
    HttpStatus status =
        ex.getMessage() != null && ex.getMessage().contains("not found")
            ? HttpStatus.NOT_FOUND
            : HttpStatus.BAD_REQUEST;
    ErrorResponse error = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), status.value());
    log.error("Order exception: ", ex);
    return new ResponseEntity<>(error, status);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(
      RuntimeException ex, WebRequest request) {
    ErrorResponse error =
        new ErrorResponse(
            LocalDateTime.now(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    log.error("Unhandled runtime exception: ", ex);
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleValidationException(
      org.springframework.web.bind.MethodArgumentNotValidException ex) {
    StringBuilder sb = new StringBuilder("Validation failed: ");
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error ->
                sb.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("; "));
    return ResponseEntity.badRequest().body(sb.toString());
  }
}
