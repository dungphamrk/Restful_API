package ra.ss15.service;

import ra.ss15.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class RevenueReportServiceImpl implements RevenueReportService {

    private final OrderRepo orderRepo;

    @Override
    public BigDecimal getRevenueByDay(LocalDate date) {
        return orderRepo.sumTotalMoneyByDate(date);
    }

    @Override
    public BigDecimal getRevenueByMonth(YearMonth month) {
        return orderRepo.sumTotalMoneyByMonth(month.getYear(), month.getMonthValue());
    }

    @Override
    public BigDecimal getRevenueByYear(int year) {
        return orderRepo.sumTotalMoneyByYear(year);
    }
}