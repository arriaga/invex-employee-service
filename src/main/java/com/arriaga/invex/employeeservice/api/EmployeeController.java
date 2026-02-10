package com.arriaga.invex.employeeservice.api;

import com.arriaga.invex.employeeservice.api.dto.EmployeeCreateRequest;
import com.arriaga.invex.employeeservice.api.dto.EmployeeResponse;
import com.arriaga.invex.employeeservice.api.dto.EmployeeUpdateRequest;
import com.arriaga.invex.employeeservice.domain.Employee;
import com.arriaga.invex.employeeservice.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/employees")
@Tag(name = "Employees", description = "Employee management endpoints")
public class EmployeeController {

  private final EmployeeService employeeService;
  private final ObjectMapper objectMapper;
  private final Validator validator;

  public EmployeeController(
      EmployeeService employeeService,
      ObjectMapper objectMapper,
      Validator validator) {
    this.employeeService = employeeService;
    this.objectMapper = objectMapper;
    this.validator = validator;
  }

  @GetMapping
  @Operation(summary = "List employees", description = "Returns all employees")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Employees returned")
  })
  public List<EmployeeResponse> getAllEmployees() {
    return employeeService.findAll().stream()
        .map(EmployeeMapper::toResponse)
        .toList();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get employee by ID", description = "Returns an employee by ID")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Employee returned"),
      @ApiResponse(responseCode = "404", description = "Employee not found",
          content = @Content(schema = @Schema(implementation = com.arriaga.invex.employeeservice.exception.ApiErrorResponse.class)))
  })
  public EmployeeResponse getEmployeeById(@PathVariable Long id) {
    Employee employee = employeeService.getById(id);
    return EmployeeMapper.toResponse(employee);
  }

  @PostMapping
  @Operation(summary = "Create employees", description = "Accepts a single employee object or an array of employees")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Employees created"),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
      @ApiResponse(responseCode = "422", description = "Validation error",
          content = @Content(schema = @Schema(implementation = com.arriaga.invex.employeeservice.exception.ApiErrorResponse.class)))
  })
  public ResponseEntity<List<EmployeeResponse>> createEmployees(
      @RequestBody JsonNode requestBody) {
    List<EmployeeCreateRequest> requests = parseCreateRequests(requestBody);
    if (requests.isEmpty()) {
      throw new IllegalArgumentException("Request body must contain at least one employee");
    }
    validateCreateRequests(requests);
    List<Employee> employees = requests.stream()
        .map(EmployeeMapper::toEntity)
        .toList();

    List<Employee> saved = employees.size() == 1
        ? List.of(employeeService.create(employees.get(0)))
        : employeeService.createAll(employees);

    List<EmployeeResponse> responses = saved.stream()
        .map(EmployeeMapper::toResponse)
        .toList();

    if (responses.size() == 1) {
      URI location = ServletUriComponentsBuilder.fromCurrentRequest()
          .path("/{id}")
          .buildAndExpand(responses.get(0).getId())
          .toUri();
      return ResponseEntity.created(location).body(responses);
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(responses);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update employee", description = "Updates provided employee fields")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Employee updated"),
      @ApiResponse(responseCode = "404", description = "Employee not found",
          content = @Content(schema = @Schema(implementation = com.arriaga.invex.employeeservice.exception.ApiErrorResponse.class))),
      @ApiResponse(responseCode = "422", description = "Validation error",
          content = @Content(schema = @Schema(implementation = com.arriaga.invex.employeeservice.exception.ApiErrorResponse.class)))
  })
  public EmployeeResponse updateEmployee(
      @PathVariable Long id,
      @Valid @RequestBody EmployeeUpdateRequest request) {
    Employee updates = new Employee();
    updates.setFirstName(request.getFirstName());
    updates.setMiddleName(request.getMiddleName());
    updates.setLastName(request.getLastName());
    updates.setSecondLastName(request.getSecondLastName());
    updates.setAge(request.getAge());
    updates.setSex(request.getSex());
    updates.setBirthDate(request.getBirthDate());
    updates.setPosition(request.getPosition());
    updates.setActive(request.getActive());

    Employee updated = employeeService.updatePartial(id, updates);
    return EmployeeMapper.toResponse(updated);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete employee", description = "Deletes an employee by ID")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Employee deleted"),
      @ApiResponse(responseCode = "404", description = "Employee not found",
          content = @Content(schema = @Schema(implementation = com.arriaga.invex.employeeservice.exception.ApiErrorResponse.class)))
  })
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/search")
  @Operation(summary = "Search employees", description = "Performs case-insensitive partial match across name fields")
  @SecurityRequirement(name = "bearerAuth")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Employees returned")
  })
  public List<EmployeeResponse> searchEmployeesByName(
      @Parameter(description = "Name fragment to search", required = true)
      @RequestParam("name") String name) {
    String normalized = name == null ? "" : name.trim().toLowerCase();
    List<EmployeeResponse> responses = new ArrayList<>();
    for (Employee employee : employeeService.findAll()) {
      String fullName = String.join(" ",
          valueOrEmpty(employee.getFirstName()),
          valueOrEmpty(employee.getMiddleName()),
          valueOrEmpty(employee.getLastName()),
          valueOrEmpty(employee.getSecondLastName())).trim().toLowerCase();
      if (!normalized.isEmpty() && fullName.contains(normalized)) {
        responses.add(EmployeeMapper.toResponse(employee));
      }
    }
    return responses;
  }

  private List<EmployeeCreateRequest> parseCreateRequests(JsonNode requestBody) {
    List<EmployeeCreateRequest> requests = new ArrayList<>();
    if (requestBody == null || requestBody.isNull()) {
      return requests;
    }
    if (requestBody.isArray()) {
      for (JsonNode node : requestBody) {
        requests.add(treeToRequest(node));
      }
      return requests;
    }
    if (requestBody.isObject()) {
      requests.add(treeToRequest(requestBody));
      return requests;
    }
    throw new IllegalArgumentException("Request body must be an object or array");
  }

  private EmployeeCreateRequest treeToRequest(JsonNode node) {
    try {
      return objectMapper.treeToValue(node, EmployeeCreateRequest.class);
    } catch (JsonProcessingException ex) {
      throw new IllegalArgumentException("Invalid request payload", ex);
    }
  }

  private void validateCreateRequests(List<EmployeeCreateRequest> requests) {
    for (EmployeeCreateRequest request : requests) {
      Set<ConstraintViolation<EmployeeCreateRequest>> violations = validator.validate(request);
      if (!violations.isEmpty()) {
        throw new ConstraintViolationException(violations);
      }
    }
  }

  private String valueOrEmpty(String value) {
    return value == null ? "" : value;
  }
}
