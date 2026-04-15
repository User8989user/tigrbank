package com.tigrbank.command.account;

import com.tigrbank.command.Command;
import com.tigrbank.facade.AccountFacade;
import java.util.Scanner;

public class UpdateAccountCommand implements Command {
    private final AccountFacade accountFacade;
    private final Scanner scanner;

    public UpdateAccountCommand(AccountFacade accountFacade, Scanner scanner) {
        this.accountFacade = accountFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Enter account ID to update: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        accountFacade.updateAccountName(id, newName);
        System.out.println("Account updated.");
    }
}