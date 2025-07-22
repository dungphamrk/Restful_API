package ra.ss10.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ra.ss10.entity.model.Account;
import ra.ss10.entity.model.CreditCard;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TransactionCreditDTO {
    private CreditCard creditCardSender;
    private Account accountReceiver;
    private Double money;
    private String note;
    private LocalDateTime createdAt;
    private String status;


}