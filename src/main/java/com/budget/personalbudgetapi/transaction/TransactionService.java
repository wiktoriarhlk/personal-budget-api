package com.budget.personalbudgetapi.transaction;

import com.budget.personalbudgetapi.account.Account;
import com.budget.personalbudgetapi.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Transaction addTransaction(Long accountId, Transaction transaction) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        transaction.setAccount(account);

        if (transaction.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        }

        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Account account = transaction.getAccount();

        if (transaction.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        }

        accountRepository.save(account);
        transactionRepository.delete(transaction);
    }

    public List<Transaction> getTransactions(Long accountId, LocalDate from, LocalDate to, String category) {
        if (from != null && to != null && category != null) {
            return transactionRepository.findByAccountIdAndDateBetweenAndCategory(accountId, from, to, category);
        } else if (from != null && to != null) {
            return transactionRepository.findByAccountIdAndDateBetween(accountId, from, to);
        } else if (category != null) {
            return transactionRepository.findByAccountIdAndCategory(accountId, category);
        } else {
            return transactionRepository.findByAccountId(accountId);
        }
    }
}