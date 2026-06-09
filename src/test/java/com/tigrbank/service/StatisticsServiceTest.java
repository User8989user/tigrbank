package com.tigrbank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StatisticsServiceTest {
    private StatisticsService statisticsService;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        statisticsService = new StatisticsService();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void record_ShouldStoreStat() {
        statisticsService.record("Test", 123L);
        statisticsService.printStatistics();
        String output = outContent.toString();
        assertTrue(output.contains("Test: 123 ms"));
    }

    @Test
    void printStatistics_WhenEmpty_ShowsMessage() {
        statisticsService.printStatistics();
        String output = outContent.toString();
        assertTrue(output.contains("No statistics recorded yet."));
    }
}