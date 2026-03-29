package com.example.gestaofinanceiraapp.data.repository;

import com.example.gestaofinanceiraapp.data.local.room.dao.BudgetDao;
import com.example.gestaofinanceiraapp.data.local.room.entity.BudgetEntity;
import com.example.gestaofinanceiraapp.data.mapper.BudgetMapper;
import com.example.gestaofinanceiraapp.domain.model.Budget;
import com.example.gestaofinanceiraapp.domain.repository.BudgetRepository;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BudgetRepositoryImpl implements BudgetRepository {
    private final BudgetDao budgetDao;

    public BudgetRepositoryImpl(BudgetDao budgetDao) {
        this.budgetDao = budgetDao;
    }

    @Override
    public void save(Budget budget) {
        budgetDao.insert(BudgetMapper.toEntity(budget));
    }

    @Override
    public List<Budget> getBudgetsByMonth(YearMonth month) {
        // Traduzindo o objeto para String que o SQLite possa entender
        String monthStr = month.toString();

        // Buscamos as entidades com o param "burro" (String)
        List<BudgetEntity> entities = budgetDao.getBudgetByMonth(monthStr);

        // Traduzimos de volta
        List<Budget> budgets = new ArrayList<>();
        for (BudgetEntity entity : entities) {
            budgets.add(BudgetMapper.toDomain(entity));
        }

        return budgets;
    }

    @Override
    public void delete(String budgetId) {
        budgetDao.delete(budgetId);
    }
}
