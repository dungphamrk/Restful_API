package ra.ss10.controller;

import com.data.ss10.model.entity.TransactionCredit;
import com.data.ss10.service.TransactionCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/credit-transactions")
public class TransactionCreditController {
    @Autowired
    private TransactionCreditService transactionCreditService;

    @PostMapping
    public ResponseEntity<TransactionCredit> createCreditTransaction(
            @RequestParam UUID creditCardId,
            @RequestParam UUID receiverAccountId,
            @RequestParam Double money,
            @RequestParam(required = false) String note) {

        TransactionCredit transaction = transactionCreditService.createCreditTransaction(
                creditCardId, receiverAccountId, money, note);

        if ("thành công".equals(transaction.getStatus())) {
            return ResponseEntity.status(201).body(transaction);
        } else {
            return ResponseEntity.status(400).body(transaction);
        }
    }
}
