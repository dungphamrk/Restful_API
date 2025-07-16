package ra.ss7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ra.ss7.model.dto.DataResponse;
import ra.ss7.service.StatisticalService;

import java.util.Map;

@Controller
@RequestMapping("/statistics")
public class StatisticalController {

    @Autowired
    private StatisticalService statisticalService;

    // GET /statistics/remaining-seeds: Thống kê số lượng hạt giống còn trong kho
    @GetMapping("/remaining-seeds")
    public ResponseEntity<DataResponse<Integer>> getRemainingSeeds() {
        int remainingSeeds = statisticalService.countRemainingSeeds();
        return new ResponseEntity<>(new DataResponse<>(remainingSeeds, HttpStatus.OK), HttpStatus.OK);
    }

    // GET /statistics/harvest-money: Thống kê sản lượng sản phẩm và tiền thu được trong tháng
    @GetMapping("/harvest-money")
    public ResponseEntity<DataResponse<Double>> getHarvestMoneyThisMonth() {
        double harvestMoney = statisticalService.totalHarvestMoneyThisMonth();
        return new ResponseEntity<>(new DataResponse<>(harvestMoney, HttpStatus.OK), HttpStatus.OK);
    }

    // GET /statistics/payment-slips: Thống kê số lượng phiếu chi và tổng số tiền
    @GetMapping("/payment-slips")
    public ResponseEntity<DataResponse<Double>> getPaymentSlipsThisMonth() {
        double paymentSlipsMoney = statisticalService.totalPaymentSlipsThisMonth();
        return new ResponseEntity<>(new DataResponse<>(paymentSlipsMoney, HttpStatus.OK), HttpStatus.OK);
    }

    // GET /statistics/profit-loss: Thống kê số tiền lãi/lỗ theo từng tháng trong năm
    @GetMapping("/profit-loss")
    public ResponseEntity<DataResponse<Map<String, Double>>> getProfitLossOverYear() {
        Map<String, Double> profitLoss = statisticalService.profitLossOverYear();
        return new ResponseEntity<>(new DataResponse<>(profitLoss, HttpStatus.OK), HttpStatus.OK);
    }

    // GET /statistics/worker-salary: Thống kê số tiền phải chi trả cho tất cả công nhân theo tháng
    @GetMapping("/worker-salary")
    public ResponseEntity<DataResponse<Double>> getWorkerSalaryThisMonth() {
        double workerSalary = statisticalService.totalWorkerSalaryThisMonth();
        return new ResponseEntity<>(new DataResponse<>(workerSalary, HttpStatus.OK), HttpStatus.OK);
    }
}