package com.budget.personalbudgetapi.account;

import com.budget.personalbudgetapi.exception.ConflictException;
import com.budget.personalbudgetapi.exception.NotFoundException;
import com.budget.personalbudgetapi.transaction.Transaction;
import com.budget.personalbudgetapi.transaction.TransactionRepository;
import com.budget.personalbudgetapi.transaction.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
    }

    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        if (!account.getTransactions().isEmpty()) {
            throw new ConflictException("Account has transactions");
        }

        accountRepository.deleteById(id);
    }

    public SummaryResponse getSummary(Long accountId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> expensesByCategory = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        return new SummaryResponse(totalIncome, totalExpense, expensesByCategory);
    }
}