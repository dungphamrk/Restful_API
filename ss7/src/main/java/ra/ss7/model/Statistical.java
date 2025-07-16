package ra.ss7.model;

import java.util.Map;

public interface Statistical {
    int countRemainingSeeds();
    double totalHarvestMoneyThisMonth();
    double totalPaymentSlipsThisMonth();
    Map<String, Double> profitLossOverYear();
    double totalWorkerSalaryThisMonth();
}