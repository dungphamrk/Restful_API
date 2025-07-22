package ra.ss10.service;

import com.data.ss10.model.entity.Account;
import com.data.ss10.model.entity.Notification;
import com.data.ss10.model.entity.Transaction;
import ra.ss10.repository.AccountRepository;
import ra.ss10.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class TransactionService {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private NotificationService notificationService;
    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.notificationService = notificationService;
    }
    public Transaction transferMoney(UUID senderId, UUID receiverId, Double money, String note) {
        Account sender = accountRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Account receiver = accountRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setMoney(money);
        transaction.setNote(note);
        transaction.setCreatedAt(LocalDateTime.now());

        if (sender.getMoney() < money) {
            transaction.setStatus("thất bại");
            transactionRepository.save(transaction);
            return transaction;
        }

        sender.setMoney(sender.getMoney() - money);
        receiver.setMoney(receiver.getMoney() + money);
        accountRepository.save(sender);
        accountRepository.save(receiver);

        transaction.setStatus("thành công");
        transactionRepository.save(transaction);

        Notification senderNoti = new Notification();
        senderNoti.setAccount(sender);
        senderNoti.setContent("Đã trừ " + money + " VNĐ. Số dư hiện tại: " + sender.getMoney());
        senderNoti.setStatus("chưa đọc");
        senderNoti.setCreatedAt(LocalDateTime.now());
        notificationService.save(senderNoti);

        Notification receiverNoti = new Notification();
        receiverNoti.setAccount(receiver);
        receiverNoti.setContent("Đã cộng " + money + " VNĐ. Số dư hiện tại: " + receiver.getMoney());
        receiverNoti.setStatus("chưa đọc");
        receiverNoti.setCreatedAt(LocalDateTime.now());
        notificationService.save(receiverNoti);

        return transaction;
    }
}
