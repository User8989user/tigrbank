package com.tigrbank.command.account;

import com.tigrbank.command.Command;
import com.tigrbank.facade.AccountFacade;
import java.util.Scanner;

public class DeleteAccountCommand implements Command {
    private final AccountFacade accountFacade;
    private final Scanner scanner;

    public DeleteAccountCommand(AccountFacade accountFacade, Scanner scanner) {
        this.accountFacade = accountFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Enter account ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());
        accountFacade.deleteAccount(id);
        System.out.println("Account deleted.");
    }
}