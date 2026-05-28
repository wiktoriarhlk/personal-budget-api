package com.budget.personalbudgetapi.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByAccountIdAndDateBetween(Long accountId, LocalDate from, LocalDate to);

    List<Transaction> findByAccountIdAndCategory(Long accountId, String category);

    List<Transaction> findByAccountIdAndDateBetweenAndCategory(Long accountId, LocalDate from, LocalDate to, String category);
}