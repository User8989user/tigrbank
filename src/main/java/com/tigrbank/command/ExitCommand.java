package com.tigrbank.command;

import spark.Spark;
public class ExitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Остановка сервера с API");
        // Останавливает SparkJava (закрывает все сокеты, освобождает порт)
        Spark.stop();
        System.out.println("Goodbye!");
        System.exit(0);
    }
}