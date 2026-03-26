package com.example.gestaofinanceiraapp.domain.model;

import java.math.BigDecimal;
import java.time.YearMonth;

// Para o sistema de metas, comparação entre o teto de gastos estipulados com as transações realizadas
public class Budget {
    private final String id;
    private final String categoryId;
    private final YearMonth monthRef;
    private final BigDecimal targetAmount;

    public Budget(String id, String categoryId, YearMonth monthRef, BigDecimal targetAmount) {
        this.id = id;
        this.categoryId = categoryId;
        this.monthRef = monthRef;
        this.targetAmount = targetAmount;
    }

    public String getId() {
        return id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public YearMonth getMonthRef() {
        return monthRef;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }
}
