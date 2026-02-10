package com.arriaga.invex.employeeservice.exception;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    List<String> details = ex.getBindingResult().getFieldErrors().stream()
        .map(this::formatFieldError)
        .collect(Collectors.toList());
    ApiErrorResponse response = buildResponse(
        request,
        ErrorCode.VALIDATION_ERROR,
        "Validation failed",
        details);
    return ResponseEntity.unprocessableEntity().body(response);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
      ConstraintViolationException ex,
      HttpServletRequest request) {
    List<String> details = ex.getConstraintViolations().stream()
        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
        .collect(Collectors.toList());
    ApiErrorResponse response = buildResponse(
        request,
        ErrorCode.VALIDATION_ERROR,
        "Validation failed",
        details);
    return ResponseEntity.unprocessableEntity().body(response);
  }

  @ExceptionHandler(EmployeeNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleEmployeeNotFound(
      EmployeeNotFoundException ex,
      HttpServletRequest request) {
    ApiErrorResponse response = buildResponse(
        request,
        ErrorCode.NOT_FOUND,
        ex.getMessage(),
        List.of());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ResponseEntity<ApiErrorResponse> handleEmptyResult(
      EmptyResultDataAccessException ex,
      HttpServletRequest request) {
    ApiErrorResponse response = buildResponse(
        request,
        ErrorCode.NOT_FOUND,
        "Employee not found",
        List.of());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
      IllegalArgumentException ex,
      HttpServletRequest request) {
    ApiErrorResponse response = buildResponse(
        request,
        ErrorCode.BAD_REQUEST,
        ex.getMessage(),
        List.of());
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiErrorResponse> handleUnreadableMessage(
      HttpMessageNotReadableException ex,
      HttpServletRequest request) {
    ApiErrorResponse response = buildResponse(
        request,
        ErrorCode.BAD_REQUEST,
        "Malformed JSON request",
        List.of());
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleException(
      Exception ex,
      HttpServletRequest request) {
    log.error(
        "Unhandled exception for {} {} corr={}",
        request.getMethod(),
        request.getRequestURI(),
        MDC.get("correlationId"),
        ex);
    ApiErrorResponse response = buildResponse(
        request,
        ErrorCode.INTERNAL_ERROR,
        "Unexpected error",
        List.of());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  private ApiErrorResponse buildResponse(
      HttpServletRequest request,
      ErrorCode errorCode,
      String message,
      List<String> details) {
    ApiErrorResponse response = new ApiErrorResponse();
    response.setTimestamp(Instant.now());
    response.setPath(request.getRequestURI());
    response.setErrorCode(errorCode.name());
    response.setMessage(message);
    response.setDetails(details == null ? new ArrayList<>() : details);
    response.setCorrelationId(MDC.get("correlationId"));
    return response;
  }

  private String formatFieldError(FieldError error) {
    return error.getField() + ": " + error.getDefaultMessage();
  }
}
