package ra.ss8.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.ss8.model.Employee;
import ra.ss8.model.dto.ApiResponse;
import ra.ss8.repository.EmployeeRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository  employeeRepository;
    public ApiResponse<Employee> createEmployee(Employee employee) {
        try {
            Employee savedEmployee = employeeRepository.save(employee);
            return new ApiResponse<>(true, "Employee created successfully", savedEmployee);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create employee: " + e.getMessage());
        }
    }

    public ApiResponse<Employee> updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + id));

        employee.setFullname(employeeDetails.getFullname());
        employee.setPhone(employeeDetails.getPhone());
        employee.setAddress(employeeDetails.getAddress());
        employee.setPosition(employeeDetails.getPosition());
        employee.setSalary(employeeDetails.getSalary());

        Employee updatedEmployee = employeeRepository.save(employee);
        return new ApiResponse<>(true, "Employee updated successfully", updatedEmployee);
    }

    public ApiResponse<String> deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new NoSuchElementException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
        return new ApiResponse<>(true, "Employee deleted successfully", null);
    }

    public ApiResponse<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return new ApiResponse<>(true, "Employees retrieved successfully", employees);
    }
}