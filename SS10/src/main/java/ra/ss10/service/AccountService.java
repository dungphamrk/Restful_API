        package ra.ss10.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ra.ss10.entity.model.Account;
import ra.ss10.entity.model.Status;
import ra.ss10.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public AccountService(AccountRepository accountRepository, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
        log.info("AccountService initialized");
    }

    public Account addAccount(Account account) {
        try {
            log.info("Adding new account for CCCD: {}", account.getCccd());
            account.setStatus(Status.ACTIVE);
            Account savedAccount = accountRepository.save(account);
            log.info("Successfully added account with ID: {}", savedAccount.getId());
            return savedAccount;
        } catch (Exception e) {
            log.error("Failed to add account for CCCD: {}. Error: {}", account.getCccd(), e.getMessage());
            throw new RuntimeException("Failed to add account", e);
        }
    }

    public Account updateAccount(Account account) {
        try {
            log.info("Updating account with ID: {}", account.getId());
            Account existingAccount = accountRepository.findById(account.getId())
                    .orElseThrow(() -> {
                        log.error("Account not found with ID: {}", account.getId());
                        return new NoSuchElementException("Account not found with ID: " + account.getId());
                    });

            // Use ModelMapper to map fields
            modelMapper.map(account, existingAccount);
            existingAccount.setStatus(existingAccount.getStatus()); // Preserve original status

            Account updatedAccount = accountRepository.save(existingAccount);
            log.info("Successfully updated account with ID: {}", updatedAccount.getId());
            return updatedAccount;
        } catch (NoSuchElementException e) {
            throw e; // Re-throw to be handled by GlobalExceptionHandler
        } catch (Exception e) {
            log.error("Failed to update account with ID: {}. Error: {}", account.getId(), e.getMessage());
            throw new RuntimeException("Failed to update account", e);
        }
    }

    public boolean closeAccount(UUID accountId) {
        try {
            log.info("Attempting to close account with ID: {}", accountId);
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> {
                        log.error("Accountಸ - Account not found with ID: {}", accountId);
                        return new NoSuchElementException("Account not found with ID: " + accountId);
                    });

            if (account.getStatus() == Status.INACTIVE) {
                log.warn("Account with ID: {} is already inactive", accountId);
                return false;
            }

            account.setStatus(Status.INACTIVE);
            accountRepository.save(account);
            log.info("Successfully closed account with ID: {}", accountId);
            return true;
        } catch (NoSuchElementException e) {
            throw e; // Re-throw to be handled by GlobalExceptionHandler
        } catch (Exception e) {
            log.error("Failed to close account with ID: {}. Error: {}", accountId, e.getMessage());
            throw new RuntimeException("Failed to close account", e);
        }
    }

    public List<Account> getAllAccounts() {
        try {
            log.info("Retrieving all accounts");
            List<Account> accounts = accountRepository.findAll();
            log.info("Successfully retrieved {} accounts", accounts.size());
            return accounts;
        } catch (Exception e) {
            log.error("Failed to retrieve accounts. Error: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve accounts", e);
        }
    }

    public Account getAccountByCccn(String cccd) {
        try {
            log.info("Retrieving account with CCCD: {}", cccd);
            Account account = accountRepository.findByCccd(cccd)
                    .orElseThrow(() -> {
                        log.error("Account not found with CCCD: {}", cccd);
                        return new NoSuchElementException("Account not found with CCCD: " + cccd);
                    });
            log.info("Successfully retrieved account with CCCD: {}", cccd);
            return account;
        } catch (NoSuchElementException e) {
            throw e; // Re-throw to be handled by GlobalExceptionHandler
        } catch (Exception e) {
            log.error("Failed to retrieve account with CCCD: {}. Error: {}", cccd, e.getMessage());
            throw new RuntimeException("Failed to retrieve account", e);
        }
    }
}
