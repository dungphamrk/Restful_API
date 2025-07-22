package ra.ss10.service;

import ra.ss10.entity.dto.CreditCardDTO;
import ra.ss10.entity.model.Account;
import ra.ss10.entity.model.CreditCard;
import ra.ss10.repository.AccountRepository;
import ra.ss10.repository.CreditCardRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class CreditCardService {
    private static final Logger log = LoggerFactory.getLogger(CreditCardService.class);
    private final CreditCardRepository creditCardRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public CreditCardService(CreditCardRepository creditCardRepository,
                             AccountRepository accountRepository,
                             ModelMapper modelMapper) {
        this.creditCardRepository = creditCardRepository;
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
        log.info("CreditCardService initialized");
    }

    public CreditCard createCreditCard(UUID accountId, Double spendingLimit) {
        try {
            log.info("Creating credit card for account ID: {}, spending limit: {}", accountId, spendingLimit);

            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> {
                        log.error("Account not found with ID: {}", accountId);
                        return new NoSuchElementException("Account not found with ID: " + accountId);
                    });

            if (creditCardRepository.existsByAccount_Id(accountId)) {
                log.error("Account already has a credit card: {}", accountId);
                throw new IllegalArgumentException("Account already has a credit card");
            }

            CreditCard creditCard = new CreditCard();
            modelMapper.map(new CreditCardDTO(account, spendingLimit, 0.0, "active"), creditCard);

            CreditCard savedCreditCard = creditCardRepository.save(creditCard);
            log.info("Successfully created credit card with ID: {}", savedCreditCard.getId());
            return savedCreditCard;
        } catch (NoSuchElementException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to create credit card for account ID: {}. Error: {}", accountId, e.getMessage());
            throw new RuntimeException("Failed to create credit card", e);
        }
    }

    public CreditCard updateSpendingLimit(UUID id, Double newLimit) {
        try {
            log.info("Updating spending limit for credit card ID: {} to {}", id, newLimit);

            CreditCard creditCard = creditCardRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Credit card not found with ID: {}", id);
                        return new NoSuchElementException("Credit card not found with ID: " + id);
                    });

            creditCard.setSpendingLimit(newLimit);
            CreditCard updatedCreditCard = creditCardRepository.save(creditCard);
            log.info("Successfully updated spending limit for credit card ID: {}", id);
            return updatedCreditCard;
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update spending limit for credit card ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update spending limit", e);
        }
    }

    public CreditCard updateStatus(UUID id, String status) {
        try {
            log.info("Updating status for credit card ID: {} to {}", id, status);

            CreditCard creditCard = creditCardRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Credit card not found with ID: {}", id);
                        return new NoSuchElementException("Credit card not found with ID: " + id);
                    });

            if (!status.equals("active") && !status.equals("inactive")) {
                log.error("Invalid status: {} for credit card ID: {}", status, id);
                throw new IllegalArgumentException("Status must be 'active' or 'inactive'");
            }

            creditCard.setStatus(status);
            CreditCard updatedCreditCard = creditCardRepository.save(creditCard);
            log.info("Successfully updated status for credit card ID: {}", id);
            return updatedCreditCard;
        } catch (NoSuchElementException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update status for credit card ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update status", e);
        }
    }


}