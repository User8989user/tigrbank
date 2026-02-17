package com.tigrbank.service;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.repository.BankAccountRepository;
import com.tigrbank.repository.OperationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private BankAccountRepository accountRepo;
    @Mock
    private OperationRepository operationRepo;
    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_ValidData_ReturnsAccount() {
        when(accountRepo.save(any(BankAccount.class))).thenAnswer(i -> i.getArgument(0));
        BankAccount account = accountService.createAccount("Test", 100.0);
        assertNotNull(account);
        assertEquals("Test", account.getName());
        assertEquals(100.0, account.getBalance());
        verify(accountRepo).save(any());
    }

    @Test
    void createAccount_NegativeBalance_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount("Test", -10));
    }

    @Test
    void updateAccountName_ExistingId_UpdatesName() {
        Long id = 1L;
        BankAccount existing = new BankAccount(id, "Old", 100.0);
        when(accountRepo.findById(id)).thenReturn(Optional.of(existing));
        when(accountRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        BankAccount updated = accountService.updateAccountName(id, "New");
        assertEquals("New", updated.getName());
        verify(accountRepo).save(existing);
    }

    @Test
    void updateAccountName_NonExisting_ThrowsException() {
        when(accountRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> accountService.updateAccountName(99L, "New"));
    }

    @Test
    void deleteAccount_WithNoOperations_Success() {
        Long id = 1L;
        when(operationRepo.findByBankAccountId(id)).thenReturn(List.of());
        accountService.deleteAccount(id);
        verify(accountRepo).delete(id);
    }

    @Test
    void deleteAccount_WithOperations_ThrowsException() {
        Long id = 1L;
        when(operationRepo.findByBankAccountId(id)).thenReturn(List.of(new Operation()));
        assertThrows(IllegalStateException.class, () -> accountService.deleteAccount(id));
    }

    @Test
    void getAllAccounts_ReturnsList() {
        List<BankAccount> expected = List.of(new BankAccount(1L, "A", 10), new BankAccount(2L, "B", 20));
        when(accountRepo.findAll()).thenReturn(expected);
        List<BankAccount> result = accountService.getAllAccounts();
        assertEquals(expected, result);
    }

    @Test
    void getAccount_ExistingId_ReturnsAccount() {
        BankAccount acc = new BankAccount(1L, "A", 10);
        when(accountRepo.findById(1L)).thenReturn(Optional.of(acc));
        BankAccount result = accountService.getAccount(1L);
        assertEquals(acc, result);
    }

    @Test
    void getAccount_NonExisting_ThrowsException() {
        when(accountRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> accountService.getAccount(1L));
    }

    @Test
    void recalculateBalance_ComputesCorrectly() {
        Long accountId = 1L;
        BankAccount account = new BankAccount(accountId, "Test", 100.0);
        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));
        List<Operation> ops = List.of(
                new Operation(1L, Type.INCOME, accountId, 50.0, LocalDate.now(), "", 1L),
                new Operation(2L, Type.EXPENSE, accountId, 30.0, LocalDate.now(), "", 2L),
                new Operation(3L, Type.INCOME, accountId, 20.0, LocalDate.now(), "", 1L)
        );
        when(operationRepo.findByBankAccountId(accountId)).thenReturn(ops);
        accountService.recalculateBalance(accountId);
        assertEquals(40.0, account.getBalance()); 
        assertEquals(40.0, account.getBalance());
        verify(accountRepo).save(account);
    }
}