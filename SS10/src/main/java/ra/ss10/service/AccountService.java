package ra.ss10.service;


import ra.ss10.entity.model.Account;
import ra.ss10.entity.model.Status;
import ra.ss10.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AccountService {
    private AccountRepository accountRepository;
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public void addAccount(Account account) {
        account.setStatus(Status.ACTIVE);

        accountRepository.save(account);
    }
    public Account updateAccount(Account account) {
        Account existingAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        existingAccount.setFullName(account.getFullName());
        existingAccount.setPhone(account.getPhone());
        existingAccount.setCccd(account.getCccd());
        existingAccount.setEmail(account.getEmail());
        existingAccount.setMoney(account.getMoney());
        existingAccount.setPassword(account.getPassword());
        return accountRepository.save(existingAccount);
    }
    public boolean closeAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        if (account.getStatus() == StatusAccount.INACTIVE) {
            return false;
        }
        account.setStatus(StatusAccount.INACTIVE);
        accountRepository.save(account);
        return true;
    }
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountByCccn(String cccn) {
        return accountRepository.findByCccn(cccn)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with CCCN: " + cccn));
    }
}
