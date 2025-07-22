package ra.ss10.controller;

import com.data.ss10.model.entity.CreditCard;
import com.data.ss10.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/creditcards")
public class CreditCardController {
    @Autowired
    private CreditCardService creditCardService;

    @PostMapping
    public ResponseEntity<CreditCard> createCreditCard(@RequestParam UUID accountId,
                                                       @RequestParam Double spendingLimit) {
        CreditCard creditCard = creditCardService.createCreditCard(accountId, spendingLimit);
        return ResponseEntity.status(201).body(creditCard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditCard> updateSpendingLimit(@PathVariable("id") String id,
                                                          @RequestParam Double spendingLimit) {
        UUID creditCardId = UUID.fromString(id);
        CreditCard updatedCreditCard = creditCardService.updateSpendingLimit(creditCardId, spendingLimit);
        return ResponseEntity.ok(updatedCreditCard);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CreditCard> updateStatus(@PathVariable("id") String id,
                                                   @RequestParam String status) {
        UUID creditCardId = UUID.fromString(id);
        CreditCard updatedCreditCard = creditCardService.updateStatus(creditCardId, status);
        return ResponseEntity.ok(updatedCreditCard);
    }
}
