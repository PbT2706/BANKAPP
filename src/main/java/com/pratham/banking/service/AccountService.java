package com.pratham.banking.service;

import com.pratham.banking.dto.AccountResponse;
import com.pratham.banking.dto.CreateAccountRequest;
import com.pratham.banking.dto.DepositRequest;
import com.pratham.banking.entity.Account;
import com.pratham.banking.entity.Transaction;
import com.pratham.banking.entity.TransactionType;
import com.pratham.banking.entity.User;
import com.pratham.banking.exception.ResourceNotFoundException;
import com.pratham.banking.repository.AccountRepository;
import com.pratham.banking.repository.TransactionRepository;
import com.pratham.banking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service class for handling account-related business logic.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
        private final TransactionRepository transactionRepository;

        public AccountService(
                        AccountRepository accountRepository,
                        UserRepository userRepository,
                        TransactionRepository transactionRepository
        ) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
                this.transactionRepository = transactionRepository;
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Account account = Account.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .build();

        Account savedAccount = accountRepository.save(account);

        return AccountResponse.builder()
                .id(savedAccount.getId())
                .userId(savedAccount.getUser().getId())
                .balance(savedAccount.getBalance())
                .createdAt(savedAccount.getCreatedAt())
                .build();
    }

    public AccountResponse getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return AccountResponse.builder()
                .id(account.getId())
                .userId(account.getUser().getId())
                .balance(account.getBalance())
                .createdAt(account.getCreatedAt())
                .build();
    }

    @Transactional
    public AccountResponse deposit(Long accountId, DepositRequest request) {
        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        BigDecimal updatedBalance = account.getBalance().add(request.getAmount());
        account.setBalance(updatedBalance);

        Account savedAccount = accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .fromAccountId(null)
                .toAccountId(accountId)
                .amount(request.getAmount())
                .type(TransactionType.DEPOSIT)
                .build();
        transactionRepository.save(transaction);

        return AccountResponse.builder()
                .id(savedAccount.getId())
                .userId(savedAccount.getUser().getId())
                .balance(savedAccount.getBalance())
                .createdAt(savedAccount.getCreatedAt())
                .build();
    }
}
