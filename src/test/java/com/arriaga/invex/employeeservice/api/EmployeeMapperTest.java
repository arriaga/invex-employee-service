package com.arriaga.invex.employeeservice.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.arriaga.invex.employeeservice.api.dto.EmployeeCreateRequest;
import com.arriaga.invex.employeeservice.api.dto.EmployeeResponse;
import com.arriaga.invex.employeeservice.api.dto.EmployeeUpdateRequest;
import com.arriaga.invex.employeeservice.domain.Employee;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class EmployeeMapperTest {

  @Test
  void mapsCreateRequestToEntity() {
    EmployeeCreateRequest request = new EmployeeCreateRequest();
    request.setFirstName("Ana");
    request.setLastName("Ruiz");
    request.setAge(30);
    request.setSex("F");
    request.setBirthDate(LocalDate.of(1994, 2, 10));
    request.setPosition("Engineer");
    request.setActive(true);

    Employee employee = EmployeeMapper.toEntity(request);

    assertThat(employee.getFirstName()).isEqualTo("Ana");
    assertThat(employee.getLastName()).isEqualTo("Ruiz");
    assertThat(employee.getAge()).isEqualTo(30);
    assertThat(employee.getSex()).isEqualTo("F");
    assertThat(employee.getBirthDate()).isEqualTo(LocalDate.of(1994, 2, 10));
    assertThat(employee.getPosition()).isEqualTo("Engineer");
    assertThat(employee.getActive()).isTrue();
  }

  @Test
  void appliesUpdatesToEntity() {
    Employee employee = new Employee();
    employee.setFirstName("Ana");
    employee.setLastName("Ruiz");

    EmployeeUpdateRequest updates = new EmployeeUpdateRequest();
    updates.setFirstName("Maria");
    updates.setAge(31);
    updates.setActive(false);

    EmployeeMapper.applyUpdates(employee, updates);

    assertThat(employee.getFirstName()).isEqualTo("Maria");
    assertThat(employee.getLastName()).isEqualTo("Ruiz");
    assertThat(employee.getAge()).isEqualTo(31);
    assertThat(employee.getActive()).isFalse();
  }

  @Test
  void mapsEntityToResponse() {
    Employee employee = new Employee();
    employee.setId(5L);
    employee.setFirstName("Ana");
    employee.setLastName("Ruiz");
    employee.setCreatedAt(Instant.parse("2026-02-10T15:00:00Z"));
    employee.setActive(true);

    EmployeeResponse response = EmployeeMapper.toResponse(employee);

    assertThat(response.getId()).isEqualTo(5L);
    assertThat(response.getFirstName()).isEqualTo("Ana");
    assertThat(response.getLastName()).isEqualTo("Ruiz");
    assertThat(response.getCreatedAt()).isEqualTo(Instant.parse("2026-02-10T15:00:00Z"));
    assertThat(response.getActive()).isTrue();
  }
}
