package ra.ss10.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ra.ss10.entity.model.Transaction;
import ra.ss10.service.TransactionService;

import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @PostMapping
    public ResponseEntity<Transaction> transferMoney(@RequestParam UUID senderId,
                                                     @RequestParam UUID receiverId,
                                                     @RequestParam Double money,
                                                     @RequestParam(required = false) String note) {
        Transaction transaction = transactionService.transferMoney(senderId, receiverId, money, note);
        if ("thành công".equals(transaction.getStatus())) {
            return ResponseEntity.status(201).body(transaction);
        } else {
            return ResponseEntity.status(400).body(transaction);
        }
    }
}
