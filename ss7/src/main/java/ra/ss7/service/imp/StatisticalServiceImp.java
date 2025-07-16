package ra.ss7.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.ss7.model.Harvest;
import ra.ss7.model.PaymentSlip;
import ra.ss7.model.Seed;
import ra.ss7.model.Worker;
import ra.ss7.repository.HarvestRepository;
import ra.ss7.repository.PaymentSlipRepository;
import ra.ss7.repository.SeedRepository;
import ra.ss7.repository.WorkerRepository;
import ra.ss7.service.StatisticalService;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticalServiceImp implements StatisticalService {

    @Autowired
    private SeedRepository seedRepository;

    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private PaymentSlipRepository paymentSlipRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Override
    public int countRemainingSeeds() {
        List<Seed> seeds = seedRepository.findAll();
        return seeds.stream().mapToInt(Seed::getQuantity).sum();
    }

    @Override
    public double totalHarvestMoneyThisMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Harvest> harvests = harvestRepository.findAll();
        return harvests.stream()
                .filter(h -> !h.getCreatedAt().isBefore(startOfMonth) && !h.getCreatedAt().isAfter(endOfMonth))
                .mapToDouble(Harvest::getTotalMoney)
                .sum();
    }

    @Override
    public double totalPaymentSlipsThisMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        List<PaymentSlip> paymentSlips = paymentSlipRepository.findAll();
        return paymentSlips.stream()
                .filter(p -> !p.getCreatedAt().isBefore(startOfMonth) && !p.getCreatedAt().isAfter(endOfMonth))
                .mapToDouble(PaymentSlip::getMoney)
                .sum();
    }

    @Override
    public Map<String, Double> profitLossOverYear() {
        Map<String, Double> profitLoss = new HashMap<>();
        int currentYear = LocalDateTime.now().getYear();

        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);
            LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            double harvestMoney = harvestRepository.findAll().stream()
                    .filter(h -> !h.getCreatedAt().isBefore(startOfMonth) && !h.getCreatedAt().isAfter(endOfMonth))
                    .mapToDouble(Harvest::getTotalMoney)
                    .sum();

            double paymentMoney = paymentSlipRepository.findAll().stream()
                    .filter(p -> !p.getCreatedAt().isBefore(startOfMonth) && !p.getCreatedAt().isAfter(endOfMonth))
                    .mapToDouble(PaymentSlip::getMoney)
                    .sum();

            double workerSalary = workerRepository.findAll().stream()
                    .mapToDouble(Worker::getSalary)
                    .sum();

            double profit = harvestMoney - (paymentMoney + workerSalary);
            profitLoss.put(yearMonth.getMonth().toString(), profit);
        }

        return profitLoss;
    }

    @Override
    public double totalWorkerSalaryThisMonth() {
        List<Worker> workers = workerRepository.findAll();
        return workers.stream()
                .mapToDouble(Worker::getSalary)
                .sum();
    }
}