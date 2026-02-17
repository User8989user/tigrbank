package com.tigrbank.service;

import java.util.List;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.domain.Operation;
import com.tigrbank.domain.Type;
import com.tigrbank.repository.BankAccountRepository;
import com.tigrbank.repository.OperationRepository;

public class AccountService {
    private final BankAccountRepository accountRepo;
    private final OperationRepository operationRepo;

    public AccountService(BankAccountRepository accountRepo, OperationRepository operationRepo) {
        this.accountRepo = accountRepo;
        this.operationRepo = operationRepo;
    }

    public BankAccount createAccount(String name, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        BankAccount account = new BankAccount(null, name, initialBalance);
        return accountRepo.save(account);
    }

    public BankAccount updateAccountName(Long id, String newName) {
        BankAccount account = accountRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + id));
        account.setName(newName);
        return accountRepo.save(account);
    }

    public void deleteAccount(Long id) {
        if (!operationRepo.findByBankAccountId(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete account with existing operations");
        }
        accountRepo.delete(id);
    }

    public List<BankAccount> getAllAccounts() {
        return accountRepo.findAll();
    }

    public BankAccount getAccount(Long id) {
        return accountRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + id));
    }

    public void recalculateBalance(Long accountId) {
        BankAccount account = getAccount(accountId);
        List<Operation> ops = operationRepo.findByBankAccountId(accountId);
        double balance = ops.stream()
                .mapToDouble(op -> op.getType() == Type.INCOME ? op.getAmount() : -op.getAmount())
                .sum();
        account.setBalance(balance);
        accountRepo.save(account);
    }
}