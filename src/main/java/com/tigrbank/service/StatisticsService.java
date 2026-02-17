package com.tigrbank.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StatisticsService {
    private final List<StatRecord> records = new ArrayList<>();

    public void record(String scenario, long durationMillis) {
        records.add(new StatRecord(scenario, durationMillis, LocalDateTime.now()));
    }

    public void printStatistics() {
        if (records.isEmpty()) {
            System.out.println("No statistics recorded yet.");
            return;
        }
        System.out.println("\n=== Execution Statistics ===");
        records.forEach(r -> System.out.printf("%s: %d ms (at %s)%n", r.scenario, r.duration, r.timestamp));
    }

    private record StatRecord(String scenario, long duration, LocalDateTime timestamp) {}
}