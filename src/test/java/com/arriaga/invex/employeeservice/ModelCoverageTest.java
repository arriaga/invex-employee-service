package com.arriaga.invex.employeeservice;

import static org.assertj.core.api.Assertions.assertThat;

import com.arriaga.invex.employeeservice.api.dto.EmployeeCreateRequest;
import com.arriaga.invex.employeeservice.api.dto.EmployeeResponse;
import com.arriaga.invex.employeeservice.api.dto.EmployeeUpdateRequest;
import com.arriaga.invex.employeeservice.api.dto.TokenRequest;
import com.arriaga.invex.employeeservice.api.dto.TokenResponse;
import com.arriaga.invex.employeeservice.domain.Employee;
import com.arriaga.invex.employeeservice.exception.ApiErrorResponse;
import com.arriaga.invex.employeeservice.exception.EmployeeNotFoundException;
import com.arriaga.invex.employeeservice.exception.ErrorCode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class ModelCoverageTest {

  @Test
  void coversEmployeeModel() {
    Employee employee = new Employee();
    employee.setId(1L);
    employee.setFirstName("Ana");
    employee.setMiddleName("Maria");
    employee.setLastName("Ruiz");
    employee.setSecondLastName("Lopez");
    employee.setAge(30);
    employee.setSex("F");
    employee.setBirthDate(LocalDate.of(1994, 2, 10));
    employee.setPosition("Engineer");
    employee.setCreatedAt(Instant.parse("2026-02-10T12:00:00Z"));
    employee.setActive(true);

    assertThat(employee.getId()).isEqualTo(1L);
    assertThat(employee.getFirstName()).isEqualTo("Ana");
    assertThat(employee.getMiddleName()).isEqualTo("Maria");
    assertThat(employee.getLastName()).isEqualTo("Ruiz");
    assertThat(employee.getSecondLastName()).isEqualTo("Lopez");
    assertThat(employee.getAge()).isEqualTo(30);
    assertThat(employee.getSex()).isEqualTo("F");
    assertThat(employee.getBirthDate()).isEqualTo(LocalDate.of(1994, 2, 10));
    assertThat(employee.getPosition()).isEqualTo("Engineer");
    assertThat(employee.getCreatedAt()).isEqualTo(Instant.parse("2026-02-10T12:00:00Z"));
    assertThat(employee.getActive()).isTrue();
  }

  @Test
  void coversEmployeeCreateRequest() {
    EmployeeCreateRequest createRequest = new EmployeeCreateRequest();
    createRequest.setFirstName("Ana");
    createRequest.setMiddleName("Maria");
    createRequest.setLastName("Ruiz");
    createRequest.setSecondLastName("Lopez");
    createRequest.setAge(30);
    createRequest.setSex("F");
    createRequest.setBirthDate(LocalDate.of(1994, 2, 10));
    createRequest.setPosition("Engineer");
    createRequest.setActive(true);

    assertThat(createRequest.getFirstName()).isEqualTo("Ana");
    assertThat(createRequest.getMiddleName()).isEqualTo("Maria");
    assertThat(createRequest.getLastName()).isEqualTo("Ruiz");
    assertThat(createRequest.getSecondLastName()).isEqualTo("Lopez");
    assertThat(createRequest.getAge()).isEqualTo(30);
    assertThat(createRequest.getSex()).isEqualTo("F");
    assertThat(createRequest.getBirthDate()).isEqualTo(LocalDate.of(1994, 2, 10));
    assertThat(createRequest.getPosition()).isEqualTo("Engineer");
    assertThat(createRequest.getActive()).isTrue();
  }

  @Test
  void coversEmployeeUpdateRequest() {
    EmployeeUpdateRequest updateRequest = new EmployeeUpdateRequest();
    updateRequest.setFirstName("Ana");
    updateRequest.setMiddleName("Maria");
    updateRequest.setLastName("Ruiz");
    updateRequest.setSecondLastName("Lopez");
    updateRequest.setAge(30);
    updateRequest.setSex("F");
    updateRequest.setBirthDate(LocalDate.of(1994, 2, 10));
    updateRequest.setPosition("Engineer");
    updateRequest.setActive(true);

    assertThat(updateRequest.getFirstName()).isEqualTo("Ana");
    assertThat(updateRequest.getMiddleName()).isEqualTo("Maria");
    assertThat(updateRequest.getLastName()).isEqualTo("Ruiz");
    assertThat(updateRequest.getSecondLastName()).isEqualTo("Lopez");
    assertThat(updateRequest.getAge()).isEqualTo(30);
    assertThat(updateRequest.getSex()).isEqualTo("F");
    assertThat(updateRequest.getBirthDate()).isEqualTo(LocalDate.of(1994, 2, 10));
    assertThat(updateRequest.getPosition()).isEqualTo("Engineer");
    assertThat(updateRequest.getActive()).isTrue();
  }

  @Test
  void coversEmployeeResponse() {
    EmployeeResponse response = new EmployeeResponse();
    response.setId(1L);
    response.setFirstName("Ana");
    response.setMiddleName("Maria");
    response.setLastName("Ruiz");
    response.setSecondLastName("Lopez");
    response.setAge(30);
    response.setSex("F");
    response.setBirthDate(LocalDate.of(1994, 2, 10));
    response.setPosition("Engineer");
    response.setCreatedAt(Instant.parse("2026-02-10T12:00:00Z"));
    response.setActive(true);

    assertThat(response.getId()).isEqualTo(1L);
    assertThat(response.getFirstName()).isEqualTo("Ana");
    assertThat(response.getMiddleName()).isEqualTo("Maria");
    assertThat(response.getLastName()).isEqualTo("Ruiz");
    assertThat(response.getSecondLastName()).isEqualTo("Lopez");
    assertThat(response.getAge()).isEqualTo(30);
    assertThat(response.getSex()).isEqualTo("F");
    assertThat(response.getBirthDate()).isEqualTo(LocalDate.of(1994, 2, 10));
    assertThat(response.getPosition()).isEqualTo("Engineer");
    assertThat(response.getCreatedAt()).isEqualTo(Instant.parse("2026-02-10T12:00:00Z"));
    assertThat(response.getActive()).isTrue();
  }

  @Test
  void coversTokenModels() {
    TokenRequest tokenRequest = new TokenRequest();
    tokenRequest.setSubject("tester");
    tokenRequest.setScopes(List.of("employee.read"));
    tokenRequest.setExpiresInMinutes(15);

    assertThat(tokenRequest.getSubject()).isEqualTo("tester");
    assertThat(tokenRequest.getScopes()).containsExactly("employee.read");
    assertThat(tokenRequest.getExpiresInMinutes()).isEqualTo(15);

    TokenResponse tokenResponse = new TokenResponse();
    tokenResponse.setAccessToken("token");
    tokenResponse.setTokenType("Bearer");
    tokenResponse.setExpiresInMinutes(15);
    tokenResponse.setScope("employee.read");

    assertThat(tokenResponse.getAccessToken()).isEqualTo("token");
    assertThat(tokenResponse.getTokenType()).isEqualTo("Bearer");
    assertThat(tokenResponse.getExpiresInMinutes()).isEqualTo(15);
    assertThat(tokenResponse.getScope()).isEqualTo("employee.read");
  }

  @Test
  void coversErrorModels() {
    ApiErrorResponse response = new ApiErrorResponse();
    response.setTimestamp(Instant.parse("2026-02-10T12:00:00Z"));
    response.setPath("/employees");
    response.setErrorCode(ErrorCode.BAD_REQUEST.name());
    response.setMessage("invalid");
    response.setDetails(List.of("detail"));
    response.setCorrelationId("corr-1");

    assertThat(response.getTimestamp()).isEqualTo(Instant.parse("2026-02-10T12:00:00Z"));
    assertThat(response.getPath()).isEqualTo("/employees");
    assertThat(response.getErrorCode()).isEqualTo(ErrorCode.BAD_REQUEST.name());
    assertThat(response.getMessage()).isEqualTo("invalid");
    assertThat(response.getDetails()).containsExactly("detail");
    assertThat(response.getCorrelationId()).isEqualTo("corr-1");

    EmployeeNotFoundException exception = new EmployeeNotFoundException(9L);
    assertThat(exception.getMessage()).contains("9");
  }
}
