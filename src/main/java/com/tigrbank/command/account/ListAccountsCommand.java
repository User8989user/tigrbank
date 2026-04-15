package com.tigrbank.command.account;

import com.tigrbank.command.Command;
import com.tigrbank.facade.AccountFacade;

public class ListAccountsCommand implements Command {
    private final AccountFacade accountFacade;

    public ListAccountsCommand(AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    @Override
    public void execute() {
        var accounts = accountFacade.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts.");
        } else {
            accounts.forEach(System.out::println);
        }
    }
}