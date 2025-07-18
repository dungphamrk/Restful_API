package ra.ss8.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ra.ss8.model.dto.ApiResponse;
import ra.ss8.service.StatisticalService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticalController {

    @Autowired
    private StatisticalService statisticalService;

    @GetMapping("/top-dishes")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTopDishes() {
        ApiResponse<List<Map<String, Object>>> response = statisticalService.getTopDishes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-customers")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTopCustomers() {
        ApiResponse<List<Map<String, Object>>> response = statisticalService.getTopCustomers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current-month-expenses")
    public ResponseEntity<ApiResponse<Double>> getCurrentMonthExpenses() {
        ApiResponse<Double> response = statisticalService.getCurrentMonthExpenses();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly-expenses")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlyExpenses(@RequestParam int year) {
        ApiResponse<List<Map<String, Object>>> response = statisticalService.getMonthlyExpenses(year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlyRevenue(@RequestParam int year) {
        ApiResponse<List<Map<String, Object>>> response = statisticalService.getMonthlyRevenue(year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-employee")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTopEmployee(@RequestParam int year, @RequestParam int month) {
        ApiResponse<Map<String, Object>> response = statisticalService.getTopEmployee(year, month);
        return ResponseEntity.ok(response);
    }
}
