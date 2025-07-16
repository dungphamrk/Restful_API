package ra.ss6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ra.ss6.model.Book;
import ra.ss6.model.DataResponse;
import ra.ss6.model.Employee;
import ra.ss6.repository.BookRepository;
import ra.ss6.repository.EmployeeRepository;
import ra.ss6.service.EmployeeService;

import java.util.List;

@Controller
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<DataResponse<List<Employee>>> getAllEmployees()  {
        return new ResponseEntity<>(new DataResponse<>(employeeService.findAllEmployees(), HttpStatus.OK),HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    public  ResponseEntity<DataResponse<Employee>> findById(@PathVariable Long empId){
        return new ResponseEntity<>(new DataResponse<>(employeeService.findEmployeeById(empId),HttpStatus.OK),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DataResponse<Employee>> saveBook(@RequestBody Employee emp){
        return new ResponseEntity<>(new DataResponse<>(employeeService.createEmployee(emp),HttpStatus.OK),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<DataResponse<Employee>> updateBook(@RequestBody Employee employee){
        return new ResponseEntity<>(new DataResponse<>(employeeService.updateEmployee(employee),HttpStatus.OK),HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long empId){
        return new ResponseEntity<>(new DataResponse<>(employeeService.deleteEmployee(empId),HttpStatus.NO_CONTENT),HttpStatus.NO_CONTENT);
    }
}
