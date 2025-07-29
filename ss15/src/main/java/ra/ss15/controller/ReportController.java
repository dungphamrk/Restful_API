package ra.ss15.controller;

import ra.ss15.service.RevenueReportService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final RevenueReportService revenueService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> getRevenue(@RequestParam String type) throws BadRequestException{
        BigDecimal revenue;
        LocalDate today = LocalDate.now();

        switch (type.toLowerCase()) {
            case "day":
                revenue = revenueService.getRevenueByDay(today);
                break;
            case "month":
                revenue = revenueService.getRevenueByMonth(YearMonth.from(today));
                break;
            case "year":
                revenue = revenueService.getRevenueByYear(today.getYear());
                break;
            default:
                throw new BadRequestException("Loại báo cáo không hợp lệ");
        }

        return ResponseEntity.ok(revenue);
    }
}
