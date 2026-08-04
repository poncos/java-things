package com.ecollado.samples.oauth_rest_sample.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "Employees", description = "Employee management endpoints")
class EmployeeController {

    @GetMapping("/api/employees")
    @Operation(summary = "Get all employees", description = "Retrieve all employees")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('SCOPE_spring-sample-api:read')")
    List<Employee> all() {
        return new ArrayList<>();
    }

    @PostMapping("/api/employees")
    @Operation(summary = "Create a new employee", description = "Add a new employee to the system")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('SCOPE_spring-sample-api:write')")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return new Employee();
    }
}
