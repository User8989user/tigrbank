package com.tigrbank.domain;

import java.time.LocalDate;

public class Operation {
    private Long id;
    private Type type;
    private Long bankAccountId;
    private double amount;
    private LocalDate date;
    private String description;
    private Long categoryId;

    public Operation() {}

    public Operation(Long id, Type type, Long bankAccountId, double amount, LocalDate date,
                     String description, Long categoryId) {
        this.id = id;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = categoryId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public Long getBankAccountId() { return bankAccountId; }
    public void setBankAccountId(Long bankAccountId) { this.bankAccountId = bankAccountId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", type=" + type +
                ", bankAccountId=" + bankAccountId +
                ", amount=" + amount +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}