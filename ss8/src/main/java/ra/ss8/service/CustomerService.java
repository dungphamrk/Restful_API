package ra.ss8.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.ss8.model.Customer;
import ra.ss8.model.dto.ApiResponse;
import ra.ss8.repository.CustomerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public ApiResponse<Customer> createCustomer(Customer customer) {
        try {
            customer.setStatus(true);
            customer.setCreatedAt(LocalDateTime.now());
            Customer savedCustomer = customerRepository.save(customer);
            return new ApiResponse<>(true, "Customer created successfully", savedCustomer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create customer: " + e.getMessage());
        }
    }

    public ApiResponse<Customer> updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer not found with id: " + id));

        if (!customer.isStatus()) {
            throw new NoSuchElementException("Customer with id: " + id + " has been deleted");
        }

        customer.setFullName(customerDetails.getFullName());
        customer.setPhone(customerDetails.getPhone());
        customer.setEmail(customerDetails.getEmail());
        customer.setNumberOfPayments(customerDetails.getNumberOfPayments());

        Customer updatedCustomer = customerRepository.save(customer);
        return new ApiResponse<>(true, "Customer updated successfully", updatedCustomer);
    }

    public ApiResponse<String> deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer not found with id: " + id));

        if (!customer.isStatus()) {
            throw new NoSuchElementException("Customer with id: " + id + " has already been deleted");
        }

        customer.setStatus(false);
        customerRepository.save(customer);
        return new ApiResponse<>(true, "Customer deleted successfully", null);
    }

    public ApiResponse<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerRepository.findByStatusTrue();
        return new ApiResponse<>(true, "Customers retrieved successfully", customers);
    }
}