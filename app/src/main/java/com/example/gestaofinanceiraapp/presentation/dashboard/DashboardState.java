package com.example.gestaofinanceiraapp.presentation.dashboard;

import java.math.BigDecimal;

// POJO para representar tudo que a tela precisa desenhar.
/*
Existe apenas para empacotar os dados formatados que a tela precisa. A tela em si deve ser "burra", sem somas ou formatações complexas,
apenas pegar esse POJO e exibir
 */
public class DashboardState {
    private final BigDecimal totalBalance;
    private final BigDecimal totalIncomes;
    private final BigDecimal totalExpenses;
    private final boolean isLoading;

    public DashboardState(BigDecimal totalBalance, BigDecimal totalIncomes, BigDecimal totalExpenses, boolean isLoading) {
        this.totalBalance = totalBalance;
        this.totalIncomes = totalIncomes;
        this.totalExpenses = totalExpenses;
        this.isLoading = isLoading;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public BigDecimal getTotalIncomes() {
        return totalIncomes;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public boolean isLoading() {
        return isLoading;
    }
}
