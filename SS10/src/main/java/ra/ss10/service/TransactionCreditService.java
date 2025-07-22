package ra.ss10.service;

import ra.ss10.entity.dto.TransactionCreditDTO;
import ra.ss10.entity.model.Account;
import ra.ss10.entity.model.CreditCard;
import ra.ss10.entity.model.Notification;
import ra.ss10.entity.model.TransactionCredit;
import ra.ss10.repository.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class TransactionCreditService {

    private static final Logger log = LoggerFactory.getLogger(TransactionCreditService.class);
    private final TransactionCreditRepository transactionCreditRepository;
    private final CreditCardRepository creditCardRepository;
    private final AccountRepository accountRepository;
    private final NotificationService notificationService;
    private final JavaMailSender mailSender;
    private final ModelMapper modelMapper;

    public TransactionCreditService(TransactionCreditRepository transactionCreditRepository,
                                    CreditCardRepository creditCardRepository,
                                    AccountRepository accountRepository,
                                    NotificationService notificationService,
                                    JavaMailSender mailSender,
                                    ModelMapper modelMapper) {
        this.transactionCreditRepository = transactionCreditRepository;
        this.creditCardRepository = creditCardRepository;
        this.accountRepository = accountRepository;
        this.notificationService = notificationService;
        this.mailSender = mailSender;
        this.modelMapper = modelMapper;
        log.info("TransactionCreditService initialized");
    }

    public TransactionCredit createCreditTransaction(UUID creditCardId, UUID receiverAccountId,
                                                     Double money, String note) {
        try {
            log.info("Starting credit transaction: {} -> {}, amount: {}", creditCardId, receiverAccountId, money);

            // Find credit card and receiver account
            CreditCard creditCard = creditCardRepository.findById(creditCardId)
                    .orElseThrow(() -> {
                        log.error("Credit card not found with ID: {}", creditCardId);
                        return new NoSuchElementException("Credit card not found with ID: " + creditCardId);
                    });
            Account receiverAccount = accountRepository.findById(receiverAccountId)
                    .orElseThrow(() -> {
                        log.error("Receiver account not found with ID: {}", receiverAccountId);
                        return new NoSuchElementException("Receiver account not found with ID: " + receiverAccountId);
                    });

            // Create transaction
            TransactionCredit transaction = createTransaction(creditCard, receiverAccount, money, note);

            // Validate and process transaction
            if (!validateTransaction(creditCard, money, transaction)) {
                return transactionCreditRepository.save(transaction);
            }

            // Process successful transaction
            return processSuccessfulTransaction(creditCard, receiverAccount, transaction);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to create credit transaction: {} -> {}. Error: {}",
                    creditCardId, receiverAccountId, e.getMessage());
            throw new RuntimeException("Failed to create credit transaction", e);
        }
    }

    private TransactionCredit createTransaction(CreditCard creditCard, Account receiverAccount, Double money, String note) {
        TransactionCredit transaction = new TransactionCredit();
        modelMapper.map(new TransactionCreditDTO(creditCard, receiverAccount, money, note, LocalDateTime.now(), "đang chờ xử lý"), transaction);
        log.info("Created transaction for credit card ID: {}", creditCard.getId());
        return transaction;
    }

    private boolean validateTransaction(CreditCard creditCard, Double money, TransactionCredit transaction) {
        // Check card status
        if (!"active".equals(creditCard.getStatus())) {
            transaction.setStatus("thất bại");
            transaction.setFailureReason("Thẻ tín dụng không hoạt động");
            log.error("Credit card inactive: {}", creditCard.getId());
            return false;
        }

        // Check spending limit
        if (creditCard.getAmountSpent() + money > creditCard.getSpendingLimit()) {
            transaction.setStatus("thất bại");
            transaction.setFailureReason(String.format("Vượt quá hạn mức: %,.0f/%,.0f VNĐ",
                    creditCard.getAmountSpent() + money, creditCard.getSpendingLimit()));
            log.error("Exceeds spending limit for card: {}", creditCard.getId());
            return false;
        }

        return true;
    }

    private TransactionCredit processSuccessfulTransaction(CreditCard creditCard, Account receiverAccount, TransactionCredit transaction) {
        try {
            // Update balances
            creditCard.setAmountSpent(creditCard.getAmountSpent() + transaction.getMoney());
            receiverAccount.setMoney(receiverAccount.getMoney() + transaction.getMoney());

            // Save changes
            creditCardRepository.save(creditCard);
            accountRepository.save(receiverAccount);

            transaction.setStatus("thành công");
            TransactionCredit savedTransaction = transactionCreditRepository.save(transaction);

            // Send notifications
            sendTransactionNotifications(creditCard, receiverAccount, transaction.getMoney());

            log.info("Successfully processed transaction: {}", savedTransaction.getId());
            return savedTransaction;
        } catch (Exception e) {
            log.error("Failed to process transaction for credit card: {}. Error: {}",
                    creditCard.getId(), e.getMessage());
            throw new RuntimeException("Failed to process transaction", e);
        }
    }

    private void sendTransactionNotifications(CreditCard creditCard, Account receiverAccount, Double money) {
        try {
            // Notify card owner
            createNotification(creditCard.getAccount(),
                    String.format("Đã thanh toán %,.0f VNĐ. Đã dùng: %,.0f/%,.0f VNĐ",
                            money, creditCard.getAmountSpent(), creditCard.getSpendingLimit()));

            // Notify receiver
            createNotification(receiverAccount,
                    String.format("Nhận %,.0f VNĐ từ thẻ tín dụng. Số dư: %,.0f VNĐ",
                            money, receiverAccount.getMoney()));
        } catch (Exception e) {
            log.error("Failed to send transaction notifications. Error: {}", e.getMessage());
            throw new RuntimeException("Failed to send transaction notifications", e);
        }
    }

    private void createNotification(Account account, String content) {
        try {
            Notification notification = new Notification();
            notification.setAccount(account);
            notification.setContent(content);
            notification.setStatus("chưa đọc");
            notification.setCreatedAt(LocalDateTime.now());
            notificationService.save(notification);
            log.info("Created notification for account ID: {}", account.getId());
        } catch (Exception e) {
            log.error("Failed to create notification for account ID: {}. Error: {}",
                    account.getId(), e.getMessage());
            throw new RuntimeException("Failed to create notification", e);
        }
    }

    @Scheduled(cron = "0 0 12 20 * *", zone = "Asia/Ho_Chi_Minh")
    public void summarizeMonthlySpendingAndNotify() {
        try {
            log.info("Starting monthly spending summary");

            YearMonth currentMonth = YearMonth.now();
            List<CreditCard> activeCreditCards = creditCardRepository.findAll().stream()
                    .filter(card -> "active".equals(card.getStatus()))
                    .toList();

            activeCreditCards.forEach(creditCard -> processMonthlyReport(creditCard, currentMonth));

            log.info("Completed monthly summary for {} credit cards", activeCreditCards.size());
        } catch (Exception e) {
            log.error("Failed to process monthly spending summary. Error: {}", e.getMessage());
            throw new RuntimeException("Failed to process monthly spending summary", e);
        }
    }

    private void processMonthlyReport(CreditCard creditCard, YearMonth month) {
        try {
            // Get monthly transactions
            List<TransactionCredit> transactions = getMonthlyTransactions(creditCard, month);
            double totalSpending = calculateTotalSpending(transactions);

            // Send summary and email
            sendMonthlySummary(creditCard.getAccount(), totalSpending, month);
            sendMonthlyEmail(creditCard.getAccount(), transactions, totalSpending, month);
        } catch (Exception e) {
            log.error("Failed to process monthly report for credit card: {}. Error: {}",
                    creditCard.getId(), e.getMessage());
            throw new RuntimeException("Failed to process monthly report", e);
        }
    }

    private List<TransactionCredit> getMonthlyTransactions(CreditCard creditCard, YearMonth month) {
        try {
            log.info("Retrieving transactions for credit card: {} for month: {}",
                    creditCard.getId(), month);
            List<TransactionCredit> transactions = transactionCreditRepository.findByCreditCardSenderAndCreatedAtBetween(
                    creditCard,
                    month.atDay(1).atStartOfDay(),
                    month.atEndOfMonth().atTime(23, 59, 59));
            log.info("Retrieved {} transactions for credit card: {}", transactions.size(), creditCard.getId());
            return transactions;
        } catch (Exception e) {
            log.error("Failed to retrieve transactions for credit card: {}. Error: {}",
                    creditCard.getId(), e.getMessage());
            throw new RuntimeException("Failed to retrieve transactions", e);
        }
    }

    private double calculateTotalSpending(List<TransactionCredit> transactions) {
        return transactions.stream()
                .filter(tr -> "thành công".equals(tr.getStatus()))
                .mapToDouble(TransactionCredit::getMoney)
                .sum();
    }

    private void sendMonthlySummary(Account account, double totalSpending, YearMonth month) {
        try {
            String content = String.format("Tổng chi tiêu tháng %02d/%d: %,.0f VNĐ",
                    month.getMonthValue(), month.getYear(), totalSpending);
            createNotification(account, content);
            log.info("Sent monthly summary notification for account: {}", account.getId());
        } catch (Exception e) {
            log.error("Failed to send monthly summary for account: {}. Error: {}",
                    account.getId(), e.getMessage());
            throw new RuntimeException("Failed to send monthly summary", e);
        }
    }

    private void sendMonthlyEmail(Account account, List<TransactionCredit> transactions,
                                  double totalSpending, YearMonth month) {
        if (account.getEmail() == null || account.getEmail().trim().isEmpty()) {
            log.warn("No email provided for account: {}", account.getId());
            return;
        }

        try {
            String emailContent = buildEmailContent(account, transactions, totalSpending, month);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(account.getEmail());
            message.setSubject(String.format("Báo cáo chi tiêu tháng %02d/%d",
                    month.getMonthValue(), month.getYear()));
            message.setText(emailContent);

            mailSender.send(message);
            log.info("Successfully sent monthly report email to: {}", account.getEmail());
        } catch (Exception e) {
            log.error("Failed to send monthly email to: {}. Error: {}",
                    account.getEmail(), e.getMessage());
            throw new RuntimeException("Failed to send monthly email", e);
        }
    }

    private String buildEmailContent(Account account, List<TransactionCredit> transactions,
                                     double totalSpending, YearMonth month) {
        StringBuilder content = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        content.append(String.format("Chào %s,\n\n", account.getFullName()));
        content.append(String.format("Báo cáo chi tiêu tháng %02d/%d\n",
                month.getMonthValue(), month.getYear()));
        content.append(String.format("Tổng chi tiêu: %,.0f VNĐ\n\n", totalSpending));

        content.append("Chi tiết giao dịch:\n");
        transactions.stream()
                .filter(tr -> "thành công".equals(tr.getStatus()))
                .forEach(tr -> content.append(String.format("- %s: %,.0f VNĐ - %s\n",
                        tr.getCreatedAt().format(formatter),
                        tr.getMoney(),
                        tr.getNote() != null ? tr.getNote() : "Không có ghi chú")));

        return content.toString();
    }

}