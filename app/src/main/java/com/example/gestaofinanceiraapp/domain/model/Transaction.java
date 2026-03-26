package com.example.gestaofinanceiraapp.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private final String id;
    private final TransactionType type;
    private final BigDecimal amount;
    private final LocalDateTime date;
    private final String categoryId;
    private final String description;

    public Transaction(String id, TransactionType type, BigDecimal amount, LocalDateTime date, String categoryId, String description) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }
}
