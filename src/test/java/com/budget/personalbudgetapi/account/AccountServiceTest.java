package com.budget.personalbudgetapi.account;

import com.budget.personalbudgetapi.exception.ConflictException;
import com.budget.personalbudgetapi.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private com.budget.personalbudgetapi.transaction.TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void shouldReturnAllAccounts() {
        Account account = new Account();
        account.setName("Test");
        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<Account> result = accountService.getAllAccounts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test");
    }

    @Test
    void shouldThrowNotFoundWhenAccountDoesNotExist() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Account not found");
    }

    @Test
    void shouldThrowConflictWhenDeletingAccountWithTransactions() {
        Account account = new Account();
        account.setId(1L);
        account.setName("Test");
        account.getTransactions().add(new com.budget.personalbudgetapi.transaction.Transaction());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.deleteAccount(1L))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Account has transactions");
    }

    @Test
    void shouldCreateAccount() {
        Account account = new Account();
        account.setName("Nowe konto");
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.createAccount(account);

        assertThat(result.getName()).isEqualTo("Nowe konto");
        verify(accountRepository).save(account);
    }
}