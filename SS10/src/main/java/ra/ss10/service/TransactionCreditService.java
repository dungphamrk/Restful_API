package ra.ss10.service;

import com.data.ss10.model.entity.*;
import com.data.ss10.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.ss10.repository.AccountRepository;
import ra.ss10.repository.CreditCardRepository;
import ra.ss10.repository.TransactionCreditRepository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class TransactionCreditService {

    private final TransactionCreditRepository transactionCreditRepository;
    private final CreditCardRepository creditCardRepository;
    private final AccountRepository accountRepository;
    private final NotificationService notificationService;

    @Autowired
    private JavaMailSender mailSender;

    public TransactionCreditService(TransactionCreditRepository transactionCreditRepository,
                                    CreditCardRepository creditCardRepository,
                                    AccountRepository accountRepository,
                                    NotificationService notificationService) {
        this.transactionCreditRepository = transactionCreditRepository;
        this.creditCardRepository = creditCardRepository;
        this.accountRepository = accountRepository;
        this.notificationService = notificationService;
    }

    public TransactionCredit createCreditTransaction(UUID creditCardId, UUID receiverAccountId,
                                                     Double money, String note) {
        log.info("Bắt đầu giao dịch thẻ tín dụng: {} -> {}, số tiền: {}", creditCardId, receiverAccountId, money);

        // Tìm thẻ và tài khoản
        CreditCard creditCard = creditCardRepository.findById(creditCardId)
                .orElseThrow(() -> new CreditCardNotFoundException("Không tìm thấy thẻ tín dụng"));
        Account receiverAccount = accountRepository.findById(receiverAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Không tìm thấy tài khoản nhận"));

        // Tạo giao dịch
        TransactionCredit transaction = createTransaction(creditCard, receiverAccount, money, note);

        // Kiểm tra và xử lý giao dịch
        if (!validateTransaction(creditCard, money, transaction)) {
            return transactionCreditRepository.save(transaction);
        }

        // Thực hiện giao dịch thành công
        return processSuccessfulTransaction(creditCard, receiverAccount, transaction);
    }

    private TransactionCredit createTransaction(CreditCard creditCard, Account receiverAccount, Double money, String note) {
        TransactionCredit transaction = new TransactionCredit();
        transaction.setCreditCardSender(creditCard);
        transaction.setAccountReceiver(receiverAccount);
        transaction.setMoney(money);
        transaction.setNote(note);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setStatus("đang chờ xử lý");
        return transaction;
    }

    private boolean validateTransaction(CreditCard creditCard, Double money, TransactionCredit transaction) {
        // Kiểm tra trạng thái thẻ
        if (!"active".equals(creditCard.getStatus())) {
            transaction.setStatus("thất bại");
            transaction.setFailureReason("Thẻ tín dụng không hoạt động");
            log.error("Thẻ không hoạt động: {}", creditCard.getId());
            return false;
        }

        // Kiểm tra hạn mức
        if (creditCard.getAmountSpent() + money > creditCard.getSpendingLimit()) {
            transaction.setStatus("thất bại");
            transaction.setFailureReason(String.format("Vượt quá hạn mức: %,.0f/%,.0f VNĐ",
                    creditCard.getAmountSpent() + money, creditCard.getSpendingLimit()));
            log.error("Vượt hạn mức thẻ: {}", creditCard.getId());
            return false;
        }

        return true;
    }

    private TransactionCredit processSuccessfulTransaction(CreditCard creditCard, Account receiverAccount, TransactionCredit transaction) {
        // Cập nhật số dư
        creditCard.setAmountSpent(creditCard.getAmountSpent() + transaction.getMoney());
        receiverAccount.setMoney(receiverAccount.getMoney() + transaction.getMoney());

        // Lưu thay đổi
        creditCardRepository.save(creditCard);
        accountRepository.save(receiverAccount);

        transaction.setStatus("thành công");
        TransactionCredit savedTransaction = transactionCreditRepository.save(transaction);

        // Gửi thông báo
        sendTransactionNotifications(creditCard, receiverAccount, transaction.getMoney());

        log.info("Giao dịch thành công: {}", savedTransaction.getId());
        return savedTransaction;
    }

    private void sendTransactionNotifications(CreditCard creditCard, Account receiverAccount, Double money) {
        // Thông báo cho chủ thẻ
        createNotification(creditCard.getAccount(),
                String.format("Đã thanh toán %,.0f VNĐ. Đã dùng: %,.0f/%,.0f VNĐ",
                        money, creditCard.getAmountSpent(), creditCard.getSpendingLimit()));

        // Thông báo cho người nhận
        createNotification(receiverAccount,
                String.format("Nhận %,.0f VNĐ từ thẻ tín dụng. Số dư: %,.0f VNĐ",
                        money, receiverAccount.getMoney()));
    }

    private void createNotification(Account account, String content) {
        Notification notification = new Notification();
        notification.setAccount(account);
        notification.setContent(content);
        notification.setStatus("chưa đọc");
        notification.setCreatedAt(LocalDateTime.now());
        notificationService.save(notification);
    }

    @Scheduled(cron = "0 0 12 20 * *", zone = "Asia/Ho_Chi_Minh")
    public void summarizeMonthlySpendingAndNotify() {
        log.info("Bắt đầu tổng hợp chi tiêu hàng tháng");

        YearMonth currentMonth = YearMonth.now();
        List<CreditCard> activeCreditCards = creditCardRepository.findAll().stream()
                .filter(card -> "active".equals(card.getStatus()))
                .toList();

        activeCreditCards.forEach(creditCard -> processMonthlyReport(creditCard, currentMonth));

        log.info("Hoàn thành tổng hợp chi tiêu cho {} thẻ", activeCreditCards.size());
    }

    private void processMonthlyReport(CreditCard creditCard, YearMonth month) {
        // Lấy giao dịch trong tháng
        List<TransactionCredit> transactions = getMonthlyTransactions(creditCard, month);
        double totalSpending = calculateTotalSpending(transactions);

        // Gửi thông báo và email
        sendMonthlySummary(creditCard.getAccount(), totalSpending, month);
        sendMonthlyEmail(creditCard.getAccount(), transactions, totalSpending, month);
    }

    private List<TransactionCredit> getMonthlyTransactions(CreditCard creditCard, YearMonth month) {
        return transactionCreditRepository.findByCreditCardSenderAndCreatedAtBetween(
                creditCard,
                month.atDay(1).atStartOfDay(),
                month.atEndOfMonth().atTime(23, 59, 59));
    }

    private double calculateTotalSpending(List<TransactionCredit> transactions) {
        return transactions.stream()
                .filter(tr -> "thành công".equals(tr.getStatus()))
                .mapToDouble(TransactionCredit::getMoney)
                .sum();
    }

    private void sendMonthlySummary(Account account, double totalSpending, YearMonth month) {
        String content = String.format("Tổng chi tiêu tháng %02d/%d: %,.0f VNĐ",
                month.getMonthValue(), month.getYear(), totalSpending);
        createNotification(account, content);
    }

    private void sendMonthlyEmail(Account account, List<TransactionCredit> transactions,
                                  double totalSpending, YearMonth month) {
        if (account.getEmail() == null || account.getEmail().trim().isEmpty()) {
            return;
        }

        String emailContent = buildEmailContent(account, transactions, totalSpending, month);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(account.getEmail());
        message.setSubject(String.format("Báo cáo chi tiêu tháng %02d/%d",
                month.getMonthValue(), month.getYear()));
        message.setText(emailContent);

        try {
            mailSender.send(message);
            log.info("Đã gửi báo cáo email cho: {}", account.getEmail());
        } catch (Exception e) {
            log.error("Lỗi gửi email: {}", e.getMessage());
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
