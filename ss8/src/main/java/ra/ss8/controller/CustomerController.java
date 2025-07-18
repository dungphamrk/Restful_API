package ra.ss8.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.ss8.model.Customer;
import ra.ss8.model.dto.ApiResponse;
import ra.ss8.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<Customer>> createCustomer(@Valid @RequestBody Customer customer) {
        ApiResponse<Customer> response = customerService.createCustomer(customer);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customerDetails) {
        ApiResponse<Customer> response = customerService.updateCustomer(id, customerDetails);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable Long id) {
        ApiResponse<String> response = customerService.deleteCustomer(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Customer>>> getAllCustomers() {
        ApiResponse<List<Customer>> response = customerService.getAllCustomers();
        return ResponseEntity.ok(response);
    }
}