package com.tigrbank.command.account;

import com.tigrbank.command.Command;
import com.tigrbank.facade.AccountFacade;
import java.util.Scanner;

public class RecalculateBalanceCommand implements Command {
    private final AccountFacade accountFacade;
    private final Scanner scanner;

    public RecalculateBalanceCommand(AccountFacade accountFacade, Scanner scanner) {
        this.accountFacade = accountFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Enter account ID to recalculate balance: ");
        Long id = Long.parseLong(scanner.nextLine());
        accountFacade.recalculateBalance(id);
        System.out.println("Balance recalculated.");
    }
}