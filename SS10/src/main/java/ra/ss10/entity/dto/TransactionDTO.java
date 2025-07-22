package ra.ss10.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ra.ss10.entity.model.Account;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TransactionDTO {
    private Account sender;
    private Account receiver;
    private Double money;
    private String note;
    private LocalDateTime createdAt;


}