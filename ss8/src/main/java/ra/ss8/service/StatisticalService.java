package ra.ss8.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.ss8.model.Order;
import ra.ss8.model.OrderDetail;
import ra.ss8.model.dto.ApiResponse;
import ra.ss8.repository.OrderDetailRepository;
import ra.ss8.repository.OrderRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class StatisticalService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public ApiResponse<List<Map<String, Object>>> getTopDishes() {
        try {
            List<Map<String, Object>> topDishes = orderDetailRepository.findAll().stream()
                    .collect(Collectors.groupingBy(
                            detail -> detail.getDish().getId(),
                            Collectors.summingInt(OrderDetail::getQuantity)
                    ))
                    .entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(10)
                    .map(entry -> {
                        OrderDetail detail = orderDetailRepository.findAll().stream()
                                .filter(d -> d.getDish().getId().equals(entry.getKey()))
                                .findFirst().orElseThrow(() -> new NoSuchElementException("Dish not found"));
                        return Map.of(
                                "dishId", (Object) detail.getDish().getId(),
                                "dishName", (Object) detail.getDish().getName(),
                                "totalQuantity", (Object) entry.getValue()
                        );
                    })
                    .collect(Collectors.toList());

            return new ApiResponse<>(true, "Top 10 dishes retrieved successfully", topDishes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve top dishes: " + e.getMessage());
        }
    }


    public ApiResponse<List<Map<String, Object>>> getTopCustomers() {
        try {
            List<Map<String, Object>> topCustomers = orderRepository.findAll().stream()
                    .collect(Collectors.groupingBy(
                            order -> order.getCustomer().getId(),
                            Collectors.summingDouble(Order::getTotalMoney)
                    ))
                    .entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(10)
                    .map(entry -> {
                        Order order = orderRepository.findAll().stream()
                                .filter(o -> o.getCustomer().getId().equals(entry.getKey()))
                                .findFirst().orElseThrow(() -> new NoSuchElementException("Customer not found"));
                        return Map.of(
                                "customerId", (Object) order.getCustomer().getId(),
                                "customerName", (Object) order.getCustomer().getFullName(),
                                "totalSpent", (Object) entry.getValue()
                        );
                    })
                    .collect(Collectors.toList());

            return new ApiResponse<>(true, "Top 10 customers retrieved successfully", topCustomers);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve top customers: " + e.getMessage());
        }
    }


    public ApiResponse<Double> getCurrentMonthExpenses() {
        try {
            YearMonth currentMonth = YearMonth.now();
            LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

            double totalExpenses = orderRepository.findAll().stream()
                    .filter(order -> !order.getCreatedAt().isBefore(startOfMonth) && !order.getCreatedAt().isAfter(endOfMonth))
                    .mapToDouble(Order::getTotalMoney)
                    .sum();

            return new ApiResponse<>(true, "Current month expenses retrieved successfully", totalExpenses);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve current month expenses: " + e.getMessage());
        }
    }

    public ApiResponse<List<Map<String, Object>>> getMonthlyExpenses(int year) {
        try {
            if (year < 1900 || year > LocalDate.now().getYear()) {
                throw new IllegalArgumentException("Invalid year: " + year);
            }

            List<Map<String, Object>> monthlyExpenses = orderRepository.findAll().stream()
                    .filter(order -> order.getCreatedAt().getYear() == year)
                    .collect(Collectors.groupingBy(
                            order -> order.getCreatedAt().getMonthValue(),
                            Collectors.summingDouble(Order::getTotalMoney)
                    ))
                    .entrySet().stream()
                    .map(entry -> Map.of(
                            "month", (Object) entry.getKey(),
                            "totalExpenses", (Object) entry.getValue()
                    ))
                    .sorted((e1, e2) -> Integer.compare((Integer) e1.get("month"), (Integer) e2.get("month")))
                    .collect(Collectors.toList());

            return new ApiResponse<>(true, "Monthly expenses for year " + year + " retrieved successfully", monthlyExpenses);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve monthly expenses: " + e.getMessage());
        }
    }

    public ApiResponse<List<Map<String, Object>>> getMonthlyRevenue(int year) {
        try {
            if (year < 1900 || year > LocalDate.now().getYear()) {
                throw new IllegalArgumentException("Invalid year: " + year);
            }

            List<Map<String, Object>> monthlyRevenue = orderRepository.findAll().stream()
                    .filter(order -> order.getCreatedAt().getYear() == year)
                    .collect(Collectors.groupingBy(
                            order -> order.getCreatedAt().getMonthValue(),
                            Collectors.summingDouble(Order::getTotalMoney)
                    ))
                    .entrySet().stream()
                    .map(entry -> Map.of(
                            "month", (Object) entry.getKey(),
                            "totalRevenue", (Object) entry.getValue()
                    ))
                    .sorted((e1, e2) -> Integer.compare((Integer) e1.get("month"), (Integer) e2.get("month")))
                    .collect(Collectors.toList());

            return new ApiResponse<>(true, "Monthly revenue for year " + year + " retrieved successfully", monthlyRevenue);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve monthly revenue: " + e.getMessage());
        }
    }


    public ApiResponse<Map<String, Object>> getTopEmployee(int year, int month) {
        try {
            if (year < 1900 || year > LocalDate.now().getYear() || month < 1 || month > 12) {
                throw new IllegalArgumentException("Invalid year or month: " + year + "-" + month);
            }

            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            Map<String, Object> topEmployee = orderRepository.findAll().stream()
                    .filter(order -> !order.getCreatedAt().isBefore(startOfMonth) && !order.getCreatedAt().isAfter(endOfMonth))
                    .collect(Collectors.groupingBy(
                            order -> order.getEmployee().getId(),
                            Collectors.summingDouble(Order::getTotalMoney)
                    ))
                    .entrySet().stream()
                    .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                    .map(entry -> {
                        Order order = orderRepository.findAll().stream()
                                .filter(o -> o.getEmployee().getId().equals(entry.getKey()))
                                .findFirst().orElseThrow(() -> new NoSuchElementException("Employee not found"));
                        return Map.of(
                                "employeeId", (Object) order.getEmployee().getId(),
                                "employeeName", (Object) order.getEmployee().getFullname(),
                                "totalRevenue", (Object) entry.getValue()
                        );
                    })
                    .orElseThrow(() -> new NoSuchElementException("No orders found for " + year + "-" + month));

            return new ApiResponse<>(true, "Top employee for " + year + "-" + month + " retrieved successfully", topEmployee);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve top employee: " + e.getMessage());
        }
    }

}