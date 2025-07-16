package ra.ss6.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.ss6.model.Employee;
import ra.ss6.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Employee newEmployee) {
        employeeRepository.findById(newEmployee.getId()).orElseThrow(()->new RuntimeException("Employee not found"));
        return employeeRepository.save(newEmployee);
    }
    public boolean deleteEmployee(Long id) {
        employeeRepository.findById(id).orElseThrow(()->new RuntimeException("Employee not found"));
        employeeRepository.deleteById(id);
        return true;
    }
    public Employee findEmployeeById(Long id){
        return employeeRepository.findById(id).orElseThrow(()->new RuntimeException("Employee not found"));
    }
    public List<Employee> findAllEmployees(){
        return employeeRepository.findAll();
    }
}
