package ra.ss8.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.ss8.model.Employee;
import ra.ss8.model.dto.ApiResponse;
import ra.ss8.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Employee>> createEmployee(@Valid @RequestBody Employee employee) {
        ApiResponse<Employee> response = employeeService.createEmployee(employee);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Employee>> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employeeDetails) {
        ApiResponse<Employee> response = employeeService.updateEmployee(id, employeeDetails);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteEmployee(@PathVariable Long id) {
        ApiResponse<String> response = employeeService.deleteEmployee(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Employee>>> getAllEmployees() {
        ApiResponse<List<Employee>> response = employeeService.getAllEmployees();
        return ResponseEntity.ok(response);
    }
}