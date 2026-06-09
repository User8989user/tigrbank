package com.tigrbank.command.account;

import com.tigrbank.command.Command;
import com.tigrbank.facade.AccountFacade;
import java.util.Scanner;

public class CreateAccountCommand implements Command {
    private final AccountFacade accountFacade;
    private final Scanner scanner;

    public CreateAccountCommand(AccountFacade accountFacade, Scanner scanner) {
        this.accountFacade = accountFacade;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Enter account name: ");
        String name = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        double balance = Double.parseDouble(scanner.nextLine());
        var account = accountFacade.createAccount(name, balance);
        System.out.println("Account created: " + account);
    }
}