package com.example.gestaofinanceiraapp.domain.repository;

import com.example.gestaofinanceiraapp.domain.model.Budget;

import java.time.YearMonth;
import java.util.List;

public interface BudgetRepository {
    void save(Budget budget);
    List<Budget> getBudgetsByMonth(YearMonth month);
    void delete(String budgetId);
}
