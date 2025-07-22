        package ra.ss10.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ra.ss10.entity.model.TransactionCredit;
import ra.ss10.service.TransactionCreditService;

import java.util.UUID;

@RestController
@RequestMapping("/credit-transactions")
public class TransactionCreditController {
    private static final Logger log = LoggerFactory.getLogger(TransactionCreditController.class);
    private final TransactionCreditService transactionCreditService;

    public TransactionCreditController(TransactionCreditService transactionCreditService) {
        this.transactionCreditService = transactionCreditService;
        log.info("TransactionCreditController initialized");
    }

    @PostMapping
    public ResponseEntity<TransactionCredit> createCreditTransaction(
            @RequestParam UUID creditCardId,
            @RequestParam UUID receiverAccountId,
            @RequestParam Double money,
            @RequestParam(required = false) String note) {
        try {
            log.info("Received request to create credit transaction: creditCardId={}, receiverAccountId={}, money={}",
                    creditCardId, receiverAccountId, money);

            if (money <= 0) {
                log.warn("Invalid money amount: {}", money);
                return ResponseEntity.badRequest().body(new TransactionCredit());
            }

            TransactionCredit transaction = transactionCreditService.createCreditTransaction(
                    creditCardId, receiverAccountId, money, note);

            if ("thành công".equals(transaction.getStatus())) {
                log.info("Credit transaction successful: {}", transaction.getId());
                return ResponseEntity.status(201).body(transaction);
            } else {
                log.warn("Credit transaction failed: {}, reason: {}", transaction.getId(), transaction.getFailureReason());
                return ResponseEntity.badRequest().body(transaction);
            }
        } catch (Exception e) {
            log.error("Error processing credit transaction: {}. Error: {}", creditCardId, e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}
