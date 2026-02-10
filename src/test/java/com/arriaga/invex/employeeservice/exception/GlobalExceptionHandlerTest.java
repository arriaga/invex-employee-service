package com.arriaga.invex.employeeservice.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @AfterEach
  void tearDown() {
    MDC.clear();
  }

  @Test
  void handleMethodArgumentNotValidBuildsResponse() {
    BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
    bindingResult.addError(new FieldError("request", "firstName", "must not be blank"));
    MethodArgumentNotValidException exception =
        new MethodArgumentNotValidException(mock(MethodParameter.class), bindingResult);

    HttpServletRequest request = mockRequest();

    ResponseEntity<ApiErrorResponse> response = handler.handleMethodArgumentNotValid(exception, request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    assertThat(response.getBody().getErrorCode()).isEqualTo(ErrorCode.VALIDATION_ERROR.name());
    assertThat(response.getBody().getDetails()).contains("firstName: must not be blank");
  }

  @Test
  void handleConstraintViolationBuildsResponse() {
    ConstraintViolation<?> violation = mock(ConstraintViolation.class);
    Path path = mock(Path.class);
    when(path.toString()).thenReturn("request.subject");
    when(violation.getPropertyPath()).thenReturn(path);
    when(violation.getMessage()).thenReturn("must not be blank");
    ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));

    HttpServletRequest request = mockRequest();

    ResponseEntity<ApiErrorResponse> response = handler.handleConstraintViolation(exception, request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    assertThat(response.getBody().getDetails()).contains("request.subject: must not be blank");
  }

  @Test
  void handleNotFoundExceptions() {
    HttpServletRequest request = mockRequest();

    ResponseEntity<ApiErrorResponse> response =
        handler.handleEmployeeNotFound(new EmployeeNotFoundException(42L), request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody().getMessage()).contains("42");
  }

  @Test
  void handleEmptyResultUsesDefaultMessage() {
    HttpServletRequest request = mockRequest();

    ResponseEntity<ApiErrorResponse> response =
        handler.handleEmptyResult(new EmptyResultDataAccessException(1), request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody().getMessage()).isEqualTo("Employee not found");
  }

  @Test
  void handleIllegalArgumentUsesMessage() {
    HttpServletRequest request = mockRequest();

    ResponseEntity<ApiErrorResponse> response =
        handler.handleIllegalArgument(new IllegalArgumentException("invalid"), request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody().getMessage()).isEqualTo("invalid");
  }

  @Test
  void handleUnreadableMessageUsesFixedMessage() {
    HttpServletRequest request = mockRequest();

    ResponseEntity<ApiErrorResponse> response =
        handler.handleUnreadableMessage(new HttpMessageNotReadableException("bad", (HttpInputMessage) null), request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody().getMessage()).isEqualTo("Malformed JSON request");
  }

  @Test
  void handleUnexpectedExceptionCapturesCorrelationId() {
    MDC.put("correlationId", "corr-123");
    HttpServletRequest request = mockRequest();

    ResponseEntity<ApiErrorResponse> response =
        handler.handleException(new RuntimeException("boom"), request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody().getCorrelationId()).isEqualTo("corr-123");
    assertThat(response.getBody().getDetails()).isEqualTo(List.of());
  }

  private HttpServletRequest mockRequest() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURI()).thenReturn("/employees");
    when(request.getMethod()).thenReturn("GET");
    return request;
  }
}
