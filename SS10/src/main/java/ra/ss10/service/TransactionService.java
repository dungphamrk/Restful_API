package ra.ss10.service;


import ra.ss10.entity.dto.TransactionDTO;
import ra.ss10.entity.model.Account;
import ra.ss10.entity.model.Notification;
import ra.ss10.entity.model.Transaction;
import ra.ss10.repository.AccountRepository;
import ra.ss10.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              NotificationService notificationService,
                              ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
        log.info("TransactionService initialized");
    }

    public Transaction transferMoney(UUID senderId, UUID receiverId, Double money, String note) {
        try {
            log.info("Starting money transfer: {} -> {}, amount: {}", senderId, receiverId, money);

            Account sender = accountRepository.findById(senderId)
                    .orElseThrow(() -> {
                        log.error("Sender account not found with ID: {}", senderId);
                        return new NoSuchElementException("Sender account not found with ID: " + senderId);
                    });
            Account receiver = accountRepository.findById(receiverId)
                    .orElseThrow(() -> {
                        log.error("Receiver account not found with ID: {}", receiverId);
                        return new NoSuchElementException("Receiver account not found with ID: " + receiverId);
                    });

            Transaction transaction = new Transaction();
            modelMapper.map(new TransactionDTO(sender, receiver, money, note, LocalDateTime.now()), transaction);

            if (sender.getMoney() < money) {
                transaction.setStatus("thất bại");
                Transaction savedTransaction = transactionRepository.save(transaction);
                log.warn("Transfer failed due to insufficient balance for sender: {}", senderId);
                return savedTransaction;
            }

            sender.setMoney(sender.getMoney() - money);
            receiver.setMoney(receiver.getMoney() + money);
            accountRepository.save(sender);
            accountRepository.save(receiver);

            transaction.setStatus("thành công");
            Transaction savedTransaction = transactionRepository.save(transaction);

            createNotifications(sender, receiver, money);

            log.info("Successfully completed transfer: {}", savedTransaction.getId());
            return savedTransaction;
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to process transfer: {} -> {}. Error: {}", senderId, receiverId, e.getMessage());
            throw new RuntimeException("Failed to process transfer", e);
        }
    }

    private void createNotifications(Account sender, Account receiver, Double money) {
        try {
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

            log.info("Created notifications for transfer: sender {}, receiver {}", sender.getId(), receiver.getId());
        } catch (Exception e) {
            log.error("Failed to create notifications for transfer. Error: {}", e.getMessage());
            throw new RuntimeException("Failed to create notifications", e);
        }
    }
}
