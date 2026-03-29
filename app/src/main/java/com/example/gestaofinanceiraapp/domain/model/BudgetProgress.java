package com.example.gestaofinanceiraapp.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/*
Criando lógica para cruzar a meta estipulada em uma categoria com a soma das despesas reais daquela mesma categoria durante o mês vigente. Aqui estamos
criando um objeto puro para representar o cálculo de fusão do orçamento da categoria e do gasto
 */
public class BudgetProgress {
    private final String budgetId;
    private final String categoryName;
    private final String categoryColor;
    private final BigDecimal targetAmount;
    private final BigDecimal spentAmount;

    public BudgetProgress(String budgetId, String categoryName, String categoryColor, BigDecimal targetAmount, BigDecimal spentAmount) {
        this.budgetId = budgetId;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.targetAmount = targetAmount;
        this.spentAmount = spentAmount;
    }

    // Calcula a porcentagem gasta
    public int getPercentageSpent() {
        if (targetAmount.compareTo(BigDecimal.ZERO) == 0) return 0;

        // (gasto / teto) * 100 utilizando operadores de BigDecimal para manter precisão
        BigDecimal percentage = spentAmount.divide(targetAmount, 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));

        // Se gastou mais que o teto, limitamos a barra de progresso a 100 para não quebrar o layout
        return Math.min(percentage.intValue(), 100);
    }

    public String getBudgetId() {
        return budgetId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }
}
