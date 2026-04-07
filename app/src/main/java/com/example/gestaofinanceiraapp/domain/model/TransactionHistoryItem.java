package com.example.gestaofinanceiraapp.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Resumo de transação pronto para exibição (histórico).
 * Mantido na camada domain para preservar a separação de responsabilidades.
 */
public class TransactionHistoryItem {
    private final String id;
    private final TransactionType type;
    private final BigDecimal signedAmount;
    private final LocalDateTime date;
    private final String categoryName;
    private final String description;

    public TransactionHistoryItem(
            String id,
            TransactionType type,
            BigDecimal signedAmount,
            LocalDateTime date,
            String categoryName,
            String description
    ) {
        this.id = id;
        this.type = type;
        this.signedAmount = signedAmount;
        this.date = date;
        this.categoryName = categoryName;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getSignedAmount() {
        return signedAmount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDescription() {
        return description;
    }
}

