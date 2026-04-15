package com.tigrbank.command;

import com.tigrbank.service.StatisticsService;

public class TimedCommand implements Command {
    private final Command delegate;
    private final StatisticsService statisticsService;

    public TimedCommand(Command delegate, StatisticsService statisticsService) {
        this.delegate = delegate;
        this.statisticsService = statisticsService;
    }

    @Override
    public void execute() {
        long start = System.currentTimeMillis();
        delegate.execute();
        long duration = System.currentTimeMillis() - start;
        statisticsService.record(delegate.getClass().getSimpleName(), duration);
        System.out.printf("[Timed] %s executed in %d ms%n", delegate.getClass().getSimpleName(), duration);
    }
}