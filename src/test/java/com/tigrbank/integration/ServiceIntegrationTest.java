package com.tigrbank.integration;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Category;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.repository.BankAccountRepository;
import com.tigrbank.repository.CategoryRepository;
import com.tigrbank.repository.OperationRepository;
import com.tigrbank.repository.impl.InMemoryBankAccountRepository;
import com.tigrbank.repository.impl.InMemoryCategoryRepository;
import com.tigrbank.repository.impl.InMemoryOperationRepository;
import com.tigrbank.service.AccountService;
import com.tigrbank.service.CategoryService;
import com.tigrbank.service.OperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceIntegrationTest {
    private BankAccountRepository accountRepo;
    private CategoryRepository categoryRepo;
    private OperationRepository operationRepo;
    private AccountService accountService;
    private CategoryService categoryService;
    private OperationService operationService;

    @BeforeEach
    void setUp() {
        accountRepo = new InMemoryBankAccountRepository();
        categoryRepo = new InMemoryCategoryRepository();
        operationRepo = new InMemoryOperationRepository();
        accountService = new AccountService(accountRepo, operationRepo);
        categoryService = new CategoryService(categoryRepo, operationRepo);
        operationService = new OperationService(operationRepo, accountRepo, categoryRepo);
    }

    @Test
    void createOperation_UpdatesAccountBalance() {
        BankAccount account = accountService.createAccount("Main", 1000.0);
        Category incomeCat = categoryService.createCategory(Type.INCOME, "Salary");
        Category expenseCat = categoryService.createCategory(Type.EXPENSE, "Food");

        operationService.createOperation(Type.INCOME, account.getId(), 500.0, LocalDate.now(), "Bonus", incomeCat.getId());
        assertEquals(1500.0, accountService.getAccount(account.getId()).getBalance());

        operationService.createOperation(Type.EXPENSE, account.getId(), 200.0, LocalDate.now(), "Dinner", expenseCat.getId());
        assertEquals(1300.0, accountService.getAccount(account.getId()).getBalance());
    }

    @Test
    void deleteOperation_RevertsBalance() {
        BankAccount account = accountService.createAccount("Main", 1000.0);
        Category cat = categoryService.createCategory(Type.INCOME, "Salary");
        Operation op = operationService.createOperation(Type.INCOME, account.getId(), 300.0, LocalDate.now(), "", cat.getId());
        assertEquals(1300.0, account.getBalance());

        operationService.deleteOperation(op.getId());
        assertEquals(1000.0, accountService.getAccount(account.getId()).getBalance());
    }
}