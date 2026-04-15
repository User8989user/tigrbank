// com.tigrbank.facade.AccountFacade.java
package com.tigrbank.facade;

import com.tigrbank.domain.BankAccount;
import com.tigrbank.service.AccountService;
import java.util.List;

public class AccountFacade {
    private final AccountService accountService;

    public AccountFacade(AccountService accountService) {
        this.accountService = accountService;
    }

    public BankAccount createAccount(String name, double initialBalance) {
        return accountService.createAccount(name, initialBalance);
    }

    public void updateAccountName(Long id, String newName) {
        accountService.updateAccountName(id, newName);
    }

    public void deleteAccount(Long id) {
        accountService.deleteAccount(id);
    }

    public List<BankAccount> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    public void recalculateBalance(Long accountId) {
        accountService.recalculateBalance(accountId);
    }
}