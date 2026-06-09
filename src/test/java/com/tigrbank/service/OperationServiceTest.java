package com.tigrbank.service;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.domain.factory.DomainObjectFactory;
import com.tigrbank.repository.BankAccountRepository;
import com.tigrbank.repository.CategoryRepository;
import com.tigrbank.repository.OperationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {
    @Mock
    private OperationRepository operationRepo;
    @Mock
    private BankAccountRepository accountRepo;
    @Mock
    private CategoryRepository categoryRepo;
    @InjectMocks
    private OperationService operationService;
    @Mock
    private DomainObjectFactory factory;

    @Test
    void createOperation_ValidIncome_UpdatesBalanceAndSaves() {
        Long accountId = 1L;
        Long categoryId = 2L;
        BankAccount account = new BankAccount(accountId, "Test", 100.0);
        Category category = new Category(categoryId, Type.INCOME, "Salary");

        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));

        // Настройка фабрики: при вызове createOperation возвращаем новый объект
        when(factory.createOperation(eq(Type.INCOME), eq(accountId), eq(50.0), any(LocalDate.class), eq("desc"),
                eq(categoryId)))
                .thenAnswer(inv -> new Operation(
                        null,
                        inv.getArgument(0),
                        inv.getArgument(1),
                        inv.getArgument(2),
                        inv.getArgument(3),
                        inv.getArgument(4),
                        inv.getArgument(5)));

        when(operationRepo.save(any(Operation.class))).thenAnswer(i -> i.getArgument(0));

        Operation op = operationService.createOperation(Type.INCOME, accountId, 50.0, LocalDate.now(), "desc",
                categoryId);

        assertNotNull(op);
        assertEquals(150.0, account.getBalance()); // баланс увеличился
        verify(operationRepo).save(any(Operation.class));
        verify(accountRepo).save(account);
        verify(factory).createOperation(Type.INCOME, accountId, 50.0, any(LocalDate.class), "desc", categoryId);
    }

    @Test
    void createOperation_ValidExpense_UpdatesBalanceAndSaves() {
        Long accountId = 1L;
        Long categoryId = 2L;
        BankAccount account = new BankAccount(accountId, "Test", 100.0);
        Category category = new Category(categoryId, Type.EXPENSE, "Food");
        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
        when(operationRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Operation op = operationService.createOperation(Type.EXPENSE, accountId, 30.0, LocalDate.now(), "desc",
                categoryId);

        assertEquals(70.0, account.getBalance());
        verify(operationRepo).save(any());
        verify(accountRepo).save(account);
    }

    @Test
    void createOperation_AccountNotFound_ThrowsException() {
        when(accountRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> operationService.createOperation(Type.INCOME, 1L, 100.0, LocalDate.now(), "desc", 1L));
    }

    @Test
    void createOperation_CategoryNotFound_ThrowsException() {
        when(accountRepo.findById(1L)).thenReturn(Optional.of(new BankAccount(1L, "A", 100)));
        when(categoryRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> operationService.createOperation(Type.INCOME, 1L, 100.0, LocalDate.now(), "desc", 1L));
    }

    @Test
    void createOperation_CategoryTypeMismatch_ThrowsException() {
        BankAccount account = new BankAccount(1L, "A", 100);
        Category category = new Category(1L, Type.EXPENSE, "Food");
        when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        assertThrows(IllegalArgumentException.class,
                () -> operationService.createOperation(Type.INCOME, 1L, 100.0, LocalDate.now(), "desc", 1L));
    }

    @Test
    void createOperation_NegativeAmount_ThrowsException() {
        BankAccount account = new BankAccount(1L, "A", 100);
        Category category = new Category(1L, Type.INCOME, "Salary");
        when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        assertThrows(IllegalArgumentException.class,
                () -> operationService.createOperation(Type.INCOME, 1L, -50.0, LocalDate.now(), "desc", 1L));
    }

    @Test
    void deleteOperation_ExistingOperation_RevertsBalanceAndDeletes() {
        Long opId = 10L;
        Long accountId = 1L;
        BankAccount account = new BankAccount(accountId, "Test", 150.0);
        Operation op = new Operation(opId, Type.INCOME, accountId, 50.0, LocalDate.now(), "desc", 2L);
        when(operationRepo.findById(opId)).thenReturn(Optional.of(op));
        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));

        operationService.deleteOperation(opId);

        assertEquals(100.0, account.getBalance()); // откат: 150 - 50 = 100
        verify(accountRepo).save(account);
        verify(operationRepo).delete(opId);
    }

    @Test
    void deleteOperation_Expense_RevertsBalanceCorrectly() {
        Long opId = 10L;
        Long accountId = 1L;
        BankAccount account = new BankAccount(accountId, "Test", 70.0);
        Operation op = new Operation(opId, Type.EXPENSE, accountId, 30.0, LocalDate.now(), "desc", 2L);
        when(operationRepo.findById(opId)).thenReturn(Optional.of(op));
        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));

        operationService.deleteOperation(opId);

        assertEquals(100.0, account.getBalance());
        verify(accountRepo).save(account);
        verify(operationRepo).delete(opId);
    }

    @Test
    void updateDescription_ExistingOperation_Updates() {
        Long opId = 1L;
        Operation op = new Operation(opId, Type.INCOME, 1L, 100.0, LocalDate.now(), "old", 1L);
        when(operationRepo.findById(opId)).thenReturn(Optional.of(op));
        when(operationRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Operation updated = operationService.updateDescription(opId, "new desc");
        assertEquals("new desc", updated.getDescription());
        verify(operationRepo).save(op);
    }

    @Test
    void updateDescription_NonExisting_ThrowsException() {
        when(operationRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> operationService.updateDescription(99L, "new"));
    }

    @Test
    void getAllOperations_DelegatesToRepo() {
        operationService.getAllOperations();
        verify(operationRepo).findAll();
    }

    @Test
    void getOperation_ExistingId_Returns() {
        Operation op = new Operation(1L, Type.INCOME, 1L, 100.0, LocalDate.now(), "", 1L);
        when(operationRepo.findById(1L)).thenReturn(Optional.of(op));
        assertEquals(op, operationService.getOperation(1L));
    }
}