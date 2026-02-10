package com.arriaga.invex.employeeservice.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.arriaga.invex.employeeservice.api.dto.EmployeeResponse;
import com.arriaga.invex.employeeservice.api.dto.EmployeeUpdateRequest;
import com.arriaga.invex.employeeservice.domain.Employee;
import com.arriaga.invex.employeeservice.service.EmployeeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

  @Mock
  private EmployeeService employeeService;

  @Captor
  private ArgumentCaptor<Employee> employeeCaptor;

  private EmployeeController controller;
  private ObjectMapper objectMapper;
  private Validator validator;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    validator = Validation.buildDefaultValidatorFactory().getValidator();
    controller = new EmployeeController(employeeService, objectMapper, validator);
  }

  @Test
  void getAllEmployeesMapsEntities() {
    Employee employee = new Employee();
    employee.setId(5L);
    employee.setFirstName("Ana");
    employee.setLastName("Ruiz");
    employee.setActive(true);

    when(employeeService.findAll()).thenReturn(List.of(employee));

    List<EmployeeResponse> responses = controller.getAllEmployees();

    assertThat(responses).hasSize(1);
    assertThat(responses.get(0).getId()).isEqualTo(5L);
    assertThat(responses.get(0).getFirstName()).isEqualTo("Ana");
  }

  @Test
  void createEmployeesCreatesSingleAndReturnsLocation() throws Exception {
    JsonNode body = objectMapper.readTree("{\"firstName\":\"Ana\",\"lastName\":\"Ruiz\",\"active\":true}");
    when(employeeService.create(any(Employee.class))).thenAnswer(invocation -> {
      Employee saved = invocation.getArgument(0);
      saved.setId(10L);
      return saved;
    });
    MockHttpServletRequest request = new MockHttpServletRequest("POST", "/employees");
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    try {
      ResponseEntity<List<EmployeeResponse>> response = controller.createEmployees(body);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
      assertThat(response.getHeaders().getLocation()).isNotNull();
      assertThat(response.getBody()).hasSize(1);
    } finally {
      RequestContextHolder.resetRequestAttributes();
    }
  }

  @Test
  void createEmployeesCreatesBatch() throws Exception {
    JsonNode body = objectMapper.readTree("[" +
        "{\"firstName\":\"Ana\",\"lastName\":\"Ruiz\",\"active\":true}," +
        "{\"firstName\":\"Luis\",\"lastName\":\"Diaz\",\"active\":true}" +
        "]");

    when(employeeService.createAll(anyList())).thenAnswer(invocation -> {
      List<Employee> employees = invocation.getArgument(0);
      for (int i = 0; i < employees.size(); i++) {
        employees.get(i).setId((long) (i + 1));
      }
      return employees;
    });

    ResponseEntity<List<EmployeeResponse>> response = controller.createEmployees(body);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getHeaders().getLocation()).isNull();
    assertThat(response.getBody()).hasSize(2);
  }

  @Test
  void createEmployeesRejectsEmptyArray() throws Exception {
    JsonNode body = objectMapper.readTree("[]");

    assertThatThrownBy(() -> controller.createEmployees(body))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("at least one employee");
  }

  @Test
  void createEmployeesRejectsInvalidPayload() throws Exception {
    JsonNode body = objectMapper.readTree("123");

    assertThatThrownBy(() -> controller.createEmployees(body))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("object or array");
  }

  @Test
  void updateEmployeeMapsRequestFields() {
    EmployeeUpdateRequest request = new EmployeeUpdateRequest();
    request.setFirstName("Ana");
    request.setLastName("Ruiz");
    request.setActive(true);

    Employee updated = new Employee();
    updated.setId(4L);
    updated.setFirstName("Ana");
    updated.setLastName("Ruiz");
    updated.setActive(true);

    when(employeeService.updatePartial(any(Long.class), any(Employee.class))).thenReturn(updated);

    EmployeeResponse response = controller.updateEmployee(4L, request);

    verify(employeeService).updatePartial(any(Long.class), employeeCaptor.capture());
    assertThat(employeeCaptor.getValue().getFirstName()).isEqualTo("Ana");
    assertThat(employeeCaptor.getValue().getLastName()).isEqualTo("Ruiz");
    assertThat(employeeCaptor.getValue().getActive()).isTrue();
    assertThat(response.getId()).isEqualTo(4L);
  }

  @Test
  void deleteEmployeeCallsService() {
    ResponseEntity<Void> response = controller.deleteEmployee(7L);

    verify(employeeService).deleteById(7L);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void searchEmployeesByNameMatchesCaseInsensitive() {
    Employee ana = new Employee();
    ana.setFirstName("Ana");
    ana.setLastName("Ruiz");
    Employee maria = new Employee();
    maria.setFirstName("Maria");
    maria.setLastName("Lopez");

    when(employeeService.findAll()).thenReturn(List.of(ana, maria));

    List<EmployeeResponse> responses = controller.searchEmployeesByName("  aNa ");

    assertThat(responses).hasSize(1);
    assertThat(responses.get(0).getFirstName()).isEqualTo("Ana");
  }
}
