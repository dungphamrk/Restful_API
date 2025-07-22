package ra.ss10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.ss10.entity.model.Account;
import ra.ss10.service.AccountService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts(){
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
    @PostMapping
    public ResponseEntity<Account> addAccount(Account account) {
        accountService.addAccount(account);
        return ResponseEntity.status(201).body(account);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") String id, @RequestBody Account account) {
        account.setId(UUID.fromString(id));
        Account updatedAccount = accountService.updateAccount(account);
        return ResponseEntity.ok(updatedAccount);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> closeAccount(@PathVariable("id") String id) {
        UUID accountId = UUID.fromString(id);
        boolean isClosed = accountService.closeAccount(accountId);
        if (isClosed) {
            return ResponseEntity.ok("Account closed successfully");
        } else {
            return ResponseEntity.status(400).body("Account is already inactive");
        }
    }
    @GetMapping("/cccn/{cccn}")
    public ResponseEntity<Account> getAccountByCccn(@PathVariable("cccn") String cccn) {
        Account account = accountService.getAccountByCccn(cccn);
        return ResponseEntity.ok(account);
    }
}
