package ra.ss10.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ra.ss10.entity.model.Account;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CreditCardDTO {
    private Account account;
    private Double spendingLimit;
    private Double amountSpent;
    private String status;
}